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
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.io.chain.ChainingTextParser;
import ezvcard.io.text.VCardReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class QVCardSerializer extends Serializer<VCard> {


    @Override
    public void write(Kryo kryo, Output output, VCard vcard) {
        byte[] bytes = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            vcard.write(baos);
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        output.write(bytes);
    }

    @Override
    public VCard read(Kryo kryo, Input input, Class<? extends VCard> aClass) {
//        byte[] bytes;
        byte[] bytes = new byte[0];
        try {
            bytes = new byte[input.available()];
        } catch (IOException e) {
            e.printStackTrace();
        }
        input.readBytes(bytes);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        VCardReader vCardReader = new VCardReader(bais);
//            VCard vcard = vCardReader.readNext();
        List<VCard> vCards = null;
        try {
            vCards = vCardReader.readAll();
            vCardReader.close();
            bais.close();
            return vCards.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
//			ChainingTextParser<ChainingTextParser<?>> p = Ezvcard.parse(bais);
//			return p.first();
        return null;
    }

    public static VCard vcardFromBytes(byte[] bytes) {

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ChainingTextParser<ChainingTextParser<?>> p = Ezvcard.parse(in);
        try {
            return p.first();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] vcardToBytes(VCard vcard) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            vcard.write(os);
            return os.toByteArray();
        }
    }

}
