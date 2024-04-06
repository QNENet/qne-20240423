/*
 * Copyright (C) 2022 Paul F Fraser <paulf@qnenet.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qnenet.qne.objects.impl.customserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.cert.*;

public class QCertificateSerializer extends Serializer<Certificate> {

    @Override
    public void write(Kryo kryo, Output output, Certificate obj) {
        try {
            byte[] bytes = obj.getEncoded();
            output.writeInt(bytes.length);
            output.writeBytes(bytes);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Certificate read(Kryo kryo, Input input, Class<? extends Certificate> aClass) {
        int size = input.readInt();
        byte[] bytes = new byte[size];
        input.readBytes(bytes);
        return certificateFromBytes(bytes);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////  Certificates /////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static X509Certificate certificateFromBytes(byte[] certificateBytes) {
        CertificateFactory certFactory;
        try {
            certFactory = CertificateFactory.getInstance("X.509");
            InputStream in = new ByteArrayInputStream(certificateBytes);
            return (X509Certificate) certFactory.generateCertificate(in);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[][] certificateArrayToByteArray(X509Certificate[] certificateChain) throws CertificateEncodingException {
        byte[][] result = new byte[3][];
        result[0] = certificateChain[0].getEncoded();
        result[1] = certificateChain[1].getEncoded();
        result[2] = certificateChain[2].getEncoded();
        return result;
    }


    public static X509Certificate[] certificateArrayFromByteArray(byte[][] certificateChainBytes) {
        X509Certificate[] certificates = new X509Certificate[3];
        certificates[0] = certificateFromBytes(certificateChainBytes[0]);
        certificates[1] = certificateFromBytes(certificateChainBytes[1]);
        certificates[2] = certificateFromBytes(certificateChainBytes[2]);
        return certificates;
    }

    public static String certificateToPem(final X509Certificate cert) {
        final StringWriter writer = new StringWriter();
        final JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
        try {
            pemWriter.writeObject(cert);
            pemWriter.flush();
            pemWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

}
