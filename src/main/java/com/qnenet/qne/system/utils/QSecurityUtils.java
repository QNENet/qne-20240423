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

package com.qnenet.qne.system.utils;

import com.qnenet.qne.objects.classes.QNoiseKeypair;
import com.southernstorm.noise.protocol.DHState;
import com.southernstorm.noise.protocol.Noise;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.encoders.Base64;

import java.io.*;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

public class QSecurityUtils { // implements QSecurityUtils {


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////// Create /////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static KeyPair createECDSAKeyPair(String keyPairAlgorithm) { // e.g. P-256, P-384
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
            keyPairGenerator.initialize(new ECGenParameterSpec(keyPairAlgorithm));
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    public static char[] generatePasswordChars(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&*()_+[{]}|;:,<.>";
        char[] options = characters.toCharArray();
        char[] result = new char[length];
        Random r = new SecureRandom();
        for (int i = 0; i < result.length; i++) {
            result[i] = options[r.nextInt(options.length)];
        }
        System.out.println("Harder Password -> " + new String(result));

        return result;
    }

    public static char[] generateLittlePassword(int length) {
        // easy input on mobile, no numbers
        String characters = "abcdefghijklmnopqrstuvwxyz";
        char[] options = characters.toCharArray();
        char[] result = new char[length];
        Random r = new SecureRandom();
        for (int i = 0; i < result.length; i++) {
            result[i] = options[r.nextInt(options.length)];
        }

        System.out.println("Simple Password -> " + new String(result));
        return result;
    }


    public static PublicKey ecdsaPublicKeyFromBytes(byte[] publicKeyEncoded) {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyEncoded);
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("ECDSA", "BC");
            return kf.generatePublic(spec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }


    public static PublicKey ecdsaPublicKeyFromPEM(String publicPEM) {
        String publicKeyPEM = publicPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        Base64 b64 = new Base64();
        @SuppressWarnings("static-access")
        byte[] decoded = b64.decode(publicKeyPEM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("ECDSA", "BC");
            return kf.generatePublic(spec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////  KeyStore /////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static KeyStore createKeyStore() {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("PKCS12", "BC");
            keyStore.load(null, null);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return keyStore;
    }

    public static KeyStore saveKeyStore(Path keyStoreFilePath, KeyStore keyStore, char[] ksPwd) {
        try {
            FileOutputStream fos = new FileOutputStream(keyStoreFilePath.toFile());
            keyStore.store(fos, ksPwd);
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return keyStore;
    }


    public static KeyStore loadKeyStore(Path keyStoreFilePath, char[] ksPwd) {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream fis = new FileInputStream(keyStoreFilePath.toFile());
            keyStore.load(fis, ksPwd);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return keyStore;

    }

    public static PrivateKey ecdsaPrivateKeyFromBytes(byte[] bytes) {
        return privateKeyFromBytes(bytes, "ECDSA");
    }

    //    for private keys use PKCS8EncodedKeySpec; for public keys use X509EncodedKeySpec

    //    DiffieHellman 	Keys for the Diffie-Hellman KeyAgreement algorithm.
//            Note: key.getAlgorithm() will return "DH" instead of "DiffieHellman".
//    DSA 	Keys for the Digital Signature Algorithm.
//    EC 	Keys for the Elliptic Curve algorithm.
//    RSA 	Keys for the RSA algorithm (Signature/Cipher).
    public static PrivateKey privateKeyFromBytes(byte[] bytes, String keyFactoryAlgorithm) {
        if (keyFactoryAlgorithm == null) {
            keyFactoryAlgorithm = "RSA";
        }
        try {
            return KeyFactory.getInstance(keyFactoryAlgorithm).generatePrivate(new PKCS8EncodedKeySpec(bytes));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////  Certificates /////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    public static QNoiseKeypair createNoiseKeypair() {
        DHState dh = null;
        try {
            dh = Noise.createDH("25519");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        dh.generateKeyPair();
        byte[] privateKeyBytes = new byte[32];
        dh.getPrivateKey(privateKeyBytes, 0);
        byte[] publicKeyBytes = new byte[32];
        dh.getPublicKey(publicKeyBytes, 0);

        QNoiseKeypair noiseKeypair = new QNoiseKeypair();
        noiseKeypair.privateKeyBytes = privateKeyBytes;
        noiseKeypair.publicKeyBytes = publicKeyBytes;
        return noiseKeypair;
    }


//    public static AsymmetricKeyParameter getEd25519PrivateKeyFromBytes(byte[] bytes) throws IOException {
//        return OpenSSHPrivateKeyUtil.parsePrivateKeyBlob(bytes);
//    }


//    public static byte[] createPin() {
//        int pin = randomIntBetween(10000, 99999);
//        return ByteBuffer.allocate(4).putInt(pin).array();
//    }
//
//
////    public static Key createKey(int passwordSize) {
////        char[] pwd = generatePasswordChars(passwordSize);
////        AESKeyFactory kf = new AESKeyFactory();
////        return kf.keyFromPassword(pwd);
////    }
//
//    public static String generateLowerCaseString(int length) {
//        String characters = "abcdefghijklmnopqrstuvwxyz"; //@#$%&*+";
//        char[] options = characters.toCharArray();
//        char[] chars = new char[length];
//        Random r = new SecureRandom();
//        for (int i = 0; i < chars.length; i++) {
//            chars[i] = options[r.nextInt(options.length)];
//        }
//        return new String(chars);
//    }
//
//
//
//    public static char[] generateSimplePassword(int length) {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; //@#$%&*+";
//        char[] options = characters.toCharArray();
//        char[] result = new char[length];
//        Random r = new SecureRandom();
//        for (int i = 0; i < result.length; i++) {
//            result[i] = options[r.nextInt(options.length)];
//        }
//
//        System.out.println("Simple Password -> " + result);
//        return result;
//    }
//
//
//
//    public static byte[] generatePasswordBytes(int length) {
//        String pwd = new String(generatePasswordChars(length));
//        try {
//            return pwd.getBytes("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////  KeyStore /////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    public static char[] getKsPwd() {
////        Properties karafWebConfigProps = QPropertyUtils.loadPropertiesFromFile(QPaths.karafWebCfgPath());
////        return ((String) karafWebConfigProps.get(QRoutes.KEYSTORE_PWD)).toCharArray();
////    }
////
////    public static KeyStore openKeystore() {
////
////        try {
////            return internalOpenKeystore(getKsPwd());
////        } catch (IOException e) {
////            e.printStackTrace();
////        } catch (NoSuchAlgorithmException e) {
////            e.printStackTrace();
////        } catch (CertificateException e) {
////            e.printStackTrace();
////        } catch (NoSuchProviderException e) {
////            e.printStackTrace();
////        } catch (KeyStoreException e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
//
//    //    public static Path keystorePath() {
////        return Paths.get(securityPath().toString(), "ssl");
////    }
////
//
////    public static KeyStore internalOpenKeystore(char[] ksPwd) throws IOException, NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException {
////        Path keyStoreFilePath = Paths.get(QPaths.keyStoreFilePath().toString());
////        QFileUtils.checkDirectory(keyStoreFilePath.getParent());
////        if (Files.notExists(keyStoreFilePath)) {
////            KeyStore keyStore = createKeyStore();
////            saveKeyStore(keyStoreFilePath, keyStore, ksPwd);
////        }
////        return loadKeyStore(keyStoreFilePath, ksPwd);
////    }
////
////    public static void saveKeystore(KeyStore keyStore, char[] ksPwd) {
////        Path keyStoreFilePath = Paths.get(QPaths.keyStoreFilePath().toString());
////        try {
////            saveKeyStore(keyStoreFilePath, keyStore, ksPwd);
////        } catch (IOException e) {
////            e.printStackTrace();
////        } catch (CertificateException e) {
////            e.printStackTrace();
////        } catch (NoSuchAlgorithmException e) {
////            e.printStackTrace();
////        } catch (KeyStoreException e) {
////            e.printStackTrace();
////        }
////    }
//
//
//
//
//    public static PrivateKey getPrivateKeyFromPEM(Path pemFilePath) throws IOException {
//        PrivateKey key;
//        FileReader reader = new FileReader(pemFilePath.toFile());
//
//        try (PEMParser pem = new PEMParser(reader)) {
//            JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
//            Object pemContent = pem.readObject();
//            if (pemContent instanceof PEMKeyPair) {
//                PEMKeyPair pemKeyPair = (PEMKeyPair) pemContent;
//                KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(pemKeyPair);
//                key = keyPair.getPrivate();
//            } else if (pemContent instanceof PrivateKeyInfo) {
//                PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemContent;
//                key = jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);
//            } else {
//                throw new IllegalArgumentException("Unsupported private key format '" +
//                        pemContent.getClass().getSimpleName() + '"');
//            }
//        }
//        return key;
//    }
//
//
////    public static RSAPublicKey readPublicKeySecondApproach(File file) throws IOException {
////        try (FileReader keyReader = new FileReader(file)) {
////            PEMParser pemParser = new PEMParser(keyReader);
////            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
////            SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
////            return (RSAPublicKey) converter.getPublicKey(publicKeyInfo);
////        }
////    }
////
////    public static RSAPrivateKey readPrivateKeySecondApproach(File file) throws IOException {
////        try (FileReader keyReader = new FileReader(file)) {
////            PEMParser pemParser = new PEMParser(keyReader);
////            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
////            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());
////            return (RSAPrivateKey) converter.getPrivateKey(privateKeyInfo);
////        }
////    }
//
////    public static void doit(File pemFile) throws IOException {
////
//////        PemUtils.
////
////        X509ExtendedKeyManager x509ExtendedKeyManager = PemUtils.loadIdentityMaterial("certificate-chain.pem", "private-key.pem");
////        x509ExtendedKeyManager.getCertificateChain("alias");
////        x509ExtendedKeyManager.
////        X509ExtendedTrustManager trustManager = PemUtils.loadTrustMaterial("some-trusted-certificate.pem");
//////
//////        sslFactory = SSLFactory.builder()
//////                .withIdentityMaterial(keyManager)
//////                .withTrustMaterial(trustManager)
//////                .build();
//////
//////        sslContext = sslFactory.getSslContext();
//////        sslSocketFactory = sslFactory.getSslSocketFactory();
////    }
//
////////////////////////////////////////////////////////////////////////////////////////////////////////
////// From https://gist.github.com/lbalmaceda/9a0c7890c2965826c04119dcfb1a5469
//////Copyright 2017 - https://github.com/lbalmaceda
//////Permission is hereby granted, free of charge, to any person obtaining a copy of this software
////// and associated documentation files (the "Software"), to deal in the Software without restriction,
////// including without limitation the rights to use, copy, modify, merge, publish, distribute,
////// sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
////// is furnished to do so, subject to the following conditions:
//////The above copyright notice and this permission notice shall be included in all copies or
////// substantial portions of the Software.
//////THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
////// INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
////// PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
////// FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
////// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
////// DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////////////////////////////
////
////    public static byte[] parsePEMFile(File pemFile) throws IOException {
////        if (!pemFile.isFile() || !pemFile.exists()) {
////            throw new FileNotFoundException(String.format("The file '%s' doesn't exist.", pemFile.getAbsolutePath()));
////        }
////        PemReader reader = new PemReader(new FileReader(pemFile));
////        PemObject pemObject = reader.readPemObject();
////        byte[] content = pemObject.getContent();
////        reader.close();
////        return content;
////    }
////
////    private static PublicKey getPublicKey(byte[] keyBytes, String algorithm) {
////        PublicKey publicKey = null;
////        try {
////            KeyFactory kf = KeyFactory.getInstance(algorithm);
////            EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
////            publicKey = kf.generatePublic(keySpec);
////        } catch (NoSuchAlgorithmException e) {
////            System.out.println("Could not reconstruct the public key, the given algorithm could not be found.");
////        } catch (InvalidKeySpecException e) {
////            System.out.println("Could not reconstruct the public key");
////        }
////
////        return publicKey;
////    }
////
////    private static PrivateKey getPrivateKey(byte[] keyBytes, String algorithm) {
////        PrivateKey privateKey = null;
////        try {
////            KeyFactory kf = KeyFactory.getInstance(algorithm);
////            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
////            privateKey = kf.generatePrivate(keySpec);
////        } catch (NoSuchAlgorithmException e) {
////            System.out.println("Could not reconstruct the private key, the given algorithm could not be found.");
////        } catch (InvalidKeySpecException e) {
////            System.out.println("Could not reconstruct the private key");
////        }
////
////        return privateKey;
////    }
////
////    public static PublicKey readPublicKeyFromFile(String filepath, String algorithm) throws IOException {
////        byte[] bytes = parsePEMFile(new File(filepath));
////        return getPublicKey(bytes, algorithm);
////    }
////
////    public static PrivateKey readPrivateKeyFromFile(String filepath, String algorithm) throws IOException {
////        byte[] bytes = parsePEMFile(new File(filepath));
////        return getPrivateKey(bytes, algorithm);
////    }
////
///////////// End Pem Utils ///////////////////////////////////////////////////////////////////////////
//
//
////    public static void pemConverter(Path keyStoreFilePath, char[] ksPwd) throws NoSuchProviderException, KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
////        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
////        return converter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
////    }
//
////    public PrivateKey getPemPrivateKey(String filename, String algorithm) throws Exception {
////        byte[] certAndKey = fileToBytes(new File(pemPath));
////        byte[] certBytes = parseDERFromPEM(certAndKey, "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
////        byte[] keyBytes = parseDERFromPEM(certAndKey, "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");
////
////        X509Certificate cert = generateCertificateFromDER(certBytes);
////        PrivateKey key  = generatePrivateKeyFromDER(keyBytes);
////    }
////
////    protected static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter) {
////        String data = new String(pem);
////        String[] tokens = data.split(beginDelimiter);
////        tokens = tokens[1].split(endDelimiter);
////        return DatatypeConverter.parseBase64Binary(tokens[0]);
////    }
////
////    protected static PrivateKey generatePrivateKeyFromDER(byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
////        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
////
////        KeyFactory factory = KeyFactory.getInstance("RSA");
////
////        return (PrivateKey)factory.generatePrivate(spec);
////    }
////
////    protected static X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException {
////        CertificateFactory factory = CertificateFactory.getInstance("X.509");
////
////        return (X509Certificate)factory.generateCertificate(new ByteArrayInputStream(certBytes));
////    }
//
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////// KeyPair ////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    c2pnb272w1
////    c2tnb191v3
////    c2pnb208w1
////    c2tnb191v2
////    c2tnb191v1
////    prime192v3
////    c2tnb359v1
////    prime192v2
////    prime192v1
////    c2tnb239v3
////    c2pnb163v3
////    c2tnb239v2
////    c2pnb163v2
////    c2tnb239v1
////    c2pnb163v1
////    c2pnb176w1
////    prime256v1
////    c2pnb304w1
////    c2pnb368w1
////    c2tnb431r1
////    prime239v3
////    prime239v2
////    prime239v1
////    sect283r1
////    sect283k1
////    sect163r2
////    secp256k1
////    secp160k1
////    secp160r1
////    secp112r2
////    secp112r1
////    sect113r2
////    sect113r1
////    sect239k1
////    secp128r2
////    sect163r1
////    secp128r1
////    sect233r1
////    sect163k1
////    sect233k1
////    sect193r2
////    sect193r1
////    sect131r2
////    sect131r1
////    secp256r1
////    sect571r1
////    sect571k1
////    secp192r1
////    sect409r1
////    sect409k1
////    secp521r1
////    secp384r1
////    secp224r1
////    secp224k1
////    secp192k1
////    secp160r2
////    B-163
////    P-521
////    P-256
////    K-163
////    B-233
////    P-224
////    P-384
////    K-233
////    B-409
////    B-283
////    B-571
////    K-409
////    K-283
////    P-192
////    K-571
////    brainpoolp512r1
////    brainpoolp384t1
////    brainpoolp256r1
////    brainpoolp192r1
////    brainpoolp512t1
////    brainpoolp256t1
////    brainpoolp224r1
////    brainpoolp320r1
////    brainpoolp192t1
////    brainpoolp160r1
////    brainpoolp224t1
////    brainpoolp384r1
////    brainpoolp320t1
////    brainpoolp160t1
////    FRP256v1
//
//
////////////  RSA  ///////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public static KeyPair getRSAKeyPair(int size) {
//        KeyPairGenerator kpg = null;
//        try {
//            kpg = KeyPairGenerator.getInstance("RSA");
//            kpg.initialize(size);
//            return kpg.generateKeyPair();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
/////////////// Elliptic keypair /////////////////////////////////////////////////////////////////////////////////////////
//
//    public static QSSHPEMKeyPair createLocalSSHEd25519PEMPair(String name) {
//        AsymmetricCipherKeyPair keyPair;
//        try {
//            keyPair = QSecurityUtils.createEd25519KeyPair();
//
//            String ed25519PrivateKeyPEM = QSecurityUtils.getEd25519PrivateKeyPEM(keyPair);
//            Path privateKeyPEMPath = Paths.get(System.getProperty("user.home"),
//                    ".ssh", "id_ed25519_" + name);
//            FileUtils.writeStringToFile(privateKeyPEMPath.toFile(), ed25519PrivateKeyPEM, Charset.forName("UTF-8"));
//
//            String ed25519PublicKeyPEM = QSecurityUtils.getEd25519PublicKeyPEM(keyPair) + " " + name;
//            Path publicKeyPEMPath = Paths.get(System.getProperty("user.home"),
//                    ".ssh", "id_ed25519_" + name + ".pub");
//            FileUtils.writeStringToFile(publicKeyPEMPath.toFile(), ed25519PublicKeyPEM, Charset.forName("UTF-8"));
//            return new QSSHPEMKeyPair(ed25519PrivateKeyPEM, ed25519PublicKeyPEM);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void removeLocalEd25519PEMPair(String name) throws IOException {
//        Path privateKeyPEMPath = Paths.get(System.getProperty("user.home"),
//                ".ssh", "id_ed25519_" + name);
//        FileUtils.delete(privateKeyPEMPath.toFile());
//
//        Path publicKeyPEMPath = Paths.get(System.getProperty("user.home"),
//                ".ssh", "id_ed25519_" + name + ".pub");
//        FileUtils.delete(publicKeyPEMPath.toFile());
//    }
//
//
//    public static AsymmetricCipherKeyPair createEd25519KeyPair() throws IOException {
//        // Security.addProvider(new BouncyCastleProvider());
//        // SecureRandom RANDOM = new SecureRandom();
//        Ed25519KeyPairGenerator keyPairGenerator = new Ed25519KeyPairGenerator();
//        keyPairGenerator.init(new Ed25519KeyGenerationParameters(new SecureRandom()));
//        return keyPairGenerator.generateKeyPair();
//    }
//
//    public static byte[] getEd25519PrivateKeyToBytes(AsymmetricCipherKeyPair keypair) throws IOException {
//        Ed25519PrivateKeyParameters privateKey = (Ed25519PrivateKeyParameters) keypair.getPrivate();
//        return OpenSSHPrivateKeyUtil.encodePrivateKey(privateKey);
//    }
//    public static String getEd25519PrivateKeyPEM(AsymmetricCipherKeyPair keypair) throws IOException {
//        Ed25519PrivateKeyParameters privateKey = (Ed25519PrivateKeyParameters) keypair.getPrivate();
//        byte[] privBytes = OpenSSHPrivateKeyUtil.encodePrivateKey(privateKey);
//        PemObject privPemObj = new PemObject("OPENSSH PRIVATE KEY", privBytes);
//        StringWriter privStringWriter = new StringWriter();
//        PemWriter privPemWriter = new PemWriter(privStringWriter);
//        try {
//            privPemWriter.writeObject(privPemObj);
//            privPemWriter.close();
//            return privStringWriter.toString();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //    private static String createAsPEM(PemObject pemObj) {
////        StringWriter privStringWriter = new StringWriter();
////        PemWriter privPemWriter = new PemWriter(privStringWriter);
////        try {
////            privPemWriter.writeObject(pemObj);
////            privPemWriter.close();
////            return privStringWriter.toString();
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
////    }
//
//
//    public static byte[] getEd25519PublicKeyToBytes(AsymmetricCipherKeyPair keypair) throws IOException {
//        Ed25519PublicKeyParameters publicKey = (Ed25519PublicKeyParameters) keypair.getPublic();
//        return OpenSSHPublicKeyUtil.encodePublicKey(publicKey);
//    }
//
//    public static AsymmetricKeyParameter getEd25519PublicKeyFromBytes(byte[] bytes) throws IOException {
//        return OpenSSHPublicKeyUtil.parsePublicKey(bytes);
//    }
//
//    public static String getEd25519PublicKeyPEM(AsymmetricCipherKeyPair keypair) throws IOException {
//        Ed25519PublicKeyParameters publicKey = (Ed25519PublicKeyParameters) keypair.getPublic();
//        return "ssh-ed25519 " + java.util.Base64.getEncoder().encodeToString(OpenSSHPublicKeyUtil.encodePublicKey(publicKey));
//    }
//
//
////    public static String getEd25519PrivatePEM(AsymmetricCipherKeyPair keypair) throws IOException {
//////        Path sshPath = Paths.get(System.getProperty("user.home"), ".ssh");
//////        Path privatePath = Paths.get(sshPath.toString(), "id_ed25519_" + name);
//////        Path publicPath = Paths.get(sshPath.toString(), "id_ed25519_" + name + ".pub");
////
////        Ed25519PrivateKeyParameters privateKey = (Ed25519PrivateKeyParameters) keypair.getPrivate();
////        byte[] privBytes = OpenSSHPrivateKeyUtil.encodePrivateKey(privateKey);
////        PemObject privPemObj = new PemObject("OPENSSH PRIVATE KEY", privBytes);
////        return createAsPEM(privPemObj);
////    }
////        FileUtils.writeStringToFile(privatePath.toFile(), privPEM, Charset.forName("UTF-8"));
////        System.out.println(privPEM);
//
//    //        /home/paulf/.ssh/id_ed25519_S550CB
////    public static String getEd25519PrivatePEM(AsymmetricCipherKeyPair keypair) throws IOException {
////
////        Ed25519PublicKeyParameters publicKey = (Ed25519PublicKeyParameters) asymmetricCipherKeyPair.getPublic();
////        String publicKeyPub = "ssh-ed25519 " + Base64.getEncoder().encodeToString(OpenSSHPublicKeyUtil.encodePublicKey(publicKey));
////        FileUtils.writeStringToFile(publicPath.toFile(), publicKeyPub, Charset.forName("UTF-8"));
////
//////        System.out.println(publicKeyPub);
////    }
//
//
////        KeyPair pair = KeyPairGenerator.getInstance("ED25519", "BC").generateKeyPair();
////        AsymmetricKeyParameter bprv = PrivateKeyFactory.createKey(pair.getPrivate().getEncoded());
////        // then proceed as you already have; I have simplified for my test environment
////        byte[] oprv = OpenSSHPrivateKeyUtil.encodePrivateKey(bprv);
////        PemWriter w = new PemWriter(new OutputStreamWriter(System.out));
////        w.writeObject(new PemObject("OPENSSH PRIVATE KEY", oprv));
////        w.close();
//
//
////    public static KeyPair getEd25519KeyPair() {
////        KeyPairGenerator kpg = null;
////        try {
////            kpg = KeyPairGenerator.getInstance("Ed25519");
////        } catch (NoSuchAlgorithmException e) {
////            throw new RuntimeException(e);
////        }
////        return kpg.generateKeyPair();
////
//////        byte[] msg = "test_string".getBytes(StandardCharsets.UTF_8);
//////
//////        Signature sig = Signature.getInstance("Ed25519");
//////        sig.initSign(kp.getPrivate());
//////        sig.update(msg);
//////        byte[] s = sig.sign();
//////
//////        String encodedString = Base64.getEncoder().encodeToString(s);
//////        System.out.println(encodedString);
////    }
//
//
////    // CMD_ecc_curve.getECCDomain().getJceName()
////    public static KeyPair getEllipticKeyPair(String jceCurveName) {
////        KeyPairGenerator kpg = null;
////        try {
////            kpg = KeyPairGenerator.getInstance("EC");
////            kpg.initialize(new ECGenParameterSpec(jceCurveName));
////            return kpg.generateKeyPair();
////        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
////
////    public static KeyPair createECKeyPair(String curveName, String algorithm) { // "prime192v1", "ECDSA"
////        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("prime192v1");
////        KeyPairGenerator keypairGenerator = null;
////        try {
////            keypairGenerator = KeyPairGenerator.getInstance(algorithm, "BC");
////            keypairGenerator.initialize(ecSpec, new SecureRandom());
////            return keypairGenerator.generateKeyPair();
////        } catch (NoSuchAlgorithmException e) {
////            e.printStackTrace();
////        } catch (NoSuchProviderException e) {
////            e.printStackTrace();
////        } catch (InvalidAlgorithmParameterException e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
//
//
//    public static byte[] publicKeyToBytes(KeyPair kp) {
//        try {
//            return kp.getPublic().getEncoded();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
////    for private keys use PKCS8EncodedKeySpec; for public keys use X509EncodedKeySpec
//
////    public static PublicKey publicKeyFromBytes(byte[] bytes, String keyType) {
////        if (keyType == null || keyType.equalsIgnoreCase("RSA")) {
////            keyType = "RSA";
////        } else {
////            keyType = "EC";
////        }
////        try {
////            return KeyFactory.getInstance(keyType).generatePublic(new X509EncodedKeySpec(bytes));
////        } catch (Exception e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////        return null;
////    }
//
//    public static ECPublicKey ecPublicKeyFromBytes(String curveName, byte[] bytes) {
//
//        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec(curveName);
//        ECCurve curve = spec.getCurve();
//        org.bouncycastle.math.ec.ECPoint g = spec.getG();
//        BigInteger n = spec.getN();
//
//        KeyFactory kf = null;
//        try {
//            kf = KeyFactory.getInstance("ECDSA", "BC");
//            ECNamedCurveSpec params = new ECNamedCurveSpec(curveName, spec.getCurve(), spec.getG(), spec.getN());
//            EllipticCurve curve1 = params.getCurve();
//            ECPoint point = ECPointUtil.decodePoint(curve1, bytes);
//            ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(point, params);
//            ECPublicKey pk = (ECPublicKey) kf.generatePublic(pubKeySpec);
//            return pk;
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchProviderException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeySpecException e) {
//            throw new RuntimeException(e);
//        }
//    }
////        KeyFactory kf = null;
////        try {
////            kf = KeyFactory.getInstance("EC");
////        byte[] x = Arrays.copyOfRange(bytes, 0, bytes.length/2);
////        byte[] y = Arrays.copyOfRange(bytes, bytes.length/2, bytes.length);
////        ECPoint ecPoint = new ECPoint(new BigInteger(1,x), new BigInteger(1,y));
////
////        AlgorithmParameters params = AlgorithmParameters.getInstance("EC");
////        params.init(new ECGenParameterSpec(curveName));
////        ECParameterSpec parameterSpec = params.getParameterSpec(ECParameterSpec.class);
////
////        return kf.generatePublic(new ECPublicKeySpec(ecPoint, parameterSpec));
////        } catch (NoSuchAlgorithmException e) {
////            throw new RuntimeException(e);
////        } catch (InvalidParameterSpecException e) {
////            throw new RuntimeException(e);
////        } catch (InvalidKeySpecException e) {
////            throw new RuntimeException(e);
////        }
////}
//
//    public static ECParameterSpec ecParameterSpecForCurve(String curveName) throws NoSuchAlgorithmException, InvalidParameterSpecException {
//        AlgorithmParameters params = AlgorithmParameters.getInstance("EC");
//        params.init(new ECGenParameterSpec(curveName));
//        return params.getParameterSpec(ECParameterSpec.class);
//    }
//
//
//    public static byte[] privateKeyToBytes(KeyPair kp) {
//        try {
//            return kp.getPrivate().getEncoded();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//
///////////  SecretKey (Symmetric) /////////////////////////////////////////////////////////////////////////////////////////
//
//    public static Key createSecretKey(String keyType, int size) {
//        KeyGenerator keyGen = null;
//        try {
//            keyGen = KeyGenerator.getInstance(keyType);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        keyGen.init(size);
//        return keyGen.generateKey();
//    }
//
//    public static byte[] createSecretKeyBytes(String keyType, int size) {
//        return createSecretKey(keyType, size).getEncoded();
//    }
//
//
//    public static void setSecretKey(KeyStore keyStore, String alias, SecretKey secretKey, char[] keyPassword) {
//        KeyStore.SecretKeyEntry secret = new KeyStore.SecretKeyEntry(secretKey);
//        KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(keyPassword);
//        try {
//            keyStore.setEntry(alias, secret, password);
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static KeyStore.Entry getSecretKey(KeyStore keyStore, String alias, char[] keyPassword) {
//        KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection(keyPassword);
//        try {
//            return keyStore.getEntry(alias, passwordProtection);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableEntryException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
///////////  PrivateKey (Asymmetric) ///////////////////////////////////////////////////////////////////////////////////////
//
//    public static void setPrivateKey(KeyStore keyStore, String alias, PrivateKey privateKey, char[] keyPassword) {
//        KeyStore.PrivateKeyEntry privateKeyEntry = new KeyStore.PrivateKeyEntry(privateKey, null);
//        KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(keyPassword);
//        try {
//            keyStore.setEntry(alias, privateKeyEntry, password);
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static KeyStore.Entry getPrivateKey(KeyStore keyStore, String alias, char[] keyPassword) {
//        KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection(keyPassword);
//        try {
//            return keyStore.getEntry(alias, passwordProtection);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableEntryException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
////
//////  Certificate Chain has client cert at idx = 0
//////    X509Certificate[] certificateChain = new X509Certificate[3];
//////    chain[0] = clientCert;
//////    chain[1] = interCert;
//////    chain[2] = caCert;
////
//////    Private key is stored with certificate chain that verifies the public static key
//
////    public static void setPrivateKeyWithChain(String alias, PrivateKey privateKey, char[] entryPassword, X509Certificate[] certificateChain) throws KeyStoreException {
////        keyStore.setKeyEntry(alias, privateKey, entryPassword, certificateChain);
////    }
////
////
////    public static void setNewCertificate(ArrayList<byte[]> certificateBytes) {
////
////    }
////
////
///////////  Trusted Certificate ///////////////////////////////////////////////////////////////////////////////////////////
////
////    public static void setTrustedCertificate(String alias, X509Certificate trustedCertificate) throws KeyStoreException {
////        keyStore.setCertificateEntry("google.com", trustedCertificate);
////    }
////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Conversions ////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
////    public static byte[] public staticKeyToBytes(KeyPair kp) {
////        try {
////            return kp.getpublic static().getEncoded();
////        } catch (Exception e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////        return null;
////    }
//
//
////    for private keys use PKCS8EncodedKeySpec; for public static keys use X509EncodedKeySpec
//
//
////    public static public staticKey public staticKeyFromBytes(byte[] bytes, String keyType) {
////        if (keyType == null) {
////            keyType = "RSA";
////        }
////        try {
////            return KeyFactory.getInstance(keyType).generatepublic static(new X509EncodedKeySpec(bytes));
////        } catch (Exception e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////        return null;
////    }
//
//
////    public static byte[] privateKeyToBytes(KeyPair kp) {
////        try {
////            return kp.getPrivate().getEncoded();
////        } catch (Exception e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////        return null;
////    }
//
////    for private keys use PKCS8EncodedKeySpec; for public static keys use X509EncodedKeySpec
//
//
////    public static PrivateKey privateKeyFromBytes(byte[] bytes, String keyType) {
////        if (keyType == null) {
////            keyType = "RSA";
////        }
////        try {
////            return KeyFactory.getInstance(keyType).generatePrivate(new PKCS8EncodedKeySpec(bytes));
////        } catch (Exception e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////        return null;
////    }
//
//
////    public static Key keyFromPassword(char[] password) {
////        AESKeyFactory kf = new AESKeyFactory();
////        return kf.keyFromPassword(password);
////    }
////
////
////    public static Key keyFromBytes(byte[] bytes) {
////        char[] password = QConvertUtils.byteArrayToCharArray(bytes);
////        return keyFromPassword(password);
////    }
////
////
////    public static Key keyFromInt(int intVal) {
////        char[] arr = String.valueOf(intVal).toCharArray();
////        return keyFromPassword(arr);
////    }
////
////
////    public static char[] passwordFromInt(int intVal) {
////        return String.valueOf(intVal).toCharArray();
////    }
////
////
////    public static Key keyFromLong(Long longVal) {
////        char[] arr = String.valueOf(longVal).toCharArray();
////        return keyFromPassword(arr);
////    }
//
//
//    public static byte[] keyToBytes(Key key) {
//        return key.getEncoded();
//    }
//
//
//    public static Key recreateKeyFromBytes(byte[] bytes) {
//        return new SecretKeySpec(bytes, 0, bytes.length, "AES");
//    }
//
//
////    public static byte[] hashPassword(byte[] password) {
////        final byte[] salt = new byte[16];
////        ThreadLocalRandom.current().nextBytes(salt);
////        int cost = 12;
////        return BCrypt.generate(password, salt, cost);
////    }
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////  Misc /////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public static int randomIntBetween(int min, int max) {
//        return ThreadLocalRandom.current().nextInt(min, max + 1);
//    }
//
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////  Main /////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////    public class ECDSA {
//
//
//    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException,
//            NoSuchProviderException, InvalidAlgorithmParameterException {
//        //  Other named curves can be found in http://www.bouncycastle.org/wiki/display/JA1/Supported+Curves+%28ECDSA+and+ECGOST%29
////        org.bouncycastle.jce.spec.ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("P-192");
////
////        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");
////
////        g.initialize(ecSpec, new SecureRandom());
////
////        return g.generateKeyPair();
//
//        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");
////        ECGenParameterSpec spec = new ECGenParameterSpec("secp256r1");
//        ECGenParameterSpec spec = new ECGenParameterSpec("P-256");
//        g.initialize(spec);
//        KeyPair keyPair = g.generateKeyPair();
//        return keyPair;
//    }
//
////        public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
////            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
////            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime256v1");
////            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
////            keyGen.initialize(ecSpec, random);
////            KeyPair pair = keyGen.generateKeyPair();
////            return pair;
////        }
//
//    public static PublicKey getPublicKey(byte[] pk) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pk);
//        KeyFactory kf = KeyFactory.getInstance("EC");
//        PublicKey pub = kf.generatePublic(publicKeySpec);
//        return pub;
//    }
//
////    public static PrivateKey getPrivateKey(byte[] privk) throws NoSuchAlgorithmException, InvalidKeySpecException {
////        EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(privk);
////        KeyFactory kf = KeyFactory.getInstance("EC");
////        PrivateKey privateKey = kf.generatePrivate(privateKeySpec);
////        return privateKey;
////    }
//
//    public static PrivateKey getPrivateKey(byte[] privk) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privk);
//        KeyFactory kf = KeyFactory.getInstance("EC");
//        PrivateKey privateKey = kf.generatePrivate(privateKeySpec);
//        return privateKey;
//    }
//
//
//    public static byte[] hexStringToByteArray(String hexString) {
//        byte[] bytes = new byte[hexString.length() / 2];
//
//        for (int i = 0; i < hexString.length(); i += 2) {
//            String sub = hexString.substring(i, i + 2);
//            Integer intVal = Integer.parseInt(sub, 16);
//            bytes[i / 2] = intVal.byteValue();
//            String hex = "".format("0x%x", bytes[i / 2]);
//        }
//        return bytes;
//    }
//
////    The conversion from a byte[] to Hex string is as follows:
//
//    public static String convertBytesToHex(byte[] bytes) {
//        char[] hexChars = new char[bytes.length * 2];
//        char[] hexArray = new char[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        return new String(hexChars).toLowerCase();
//    }
////    }
//
//
//
//    public static String ecdsaPublicKeyToPEM(PublicKey ecdsaPublicKey) {
//        StringWriter writer = new StringWriter();
//        JcaPEMWriter publicPemWriter = new JcaPEMWriter(writer);
//        try {
//            publicPemWriter.writeObject(ecdsaPublicKey);
//            publicPemWriter.close();
//            return writer.toString();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                writer.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//
//    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeySpecException {
//
//
//        //        Security.addProvider(new BouncyCastleProvider());
////
////        KeyPair keyPair = generateKeyPair();
////        PublicKey publicKey = keyPair.getPublic();
////        PrivateKey privateKey = keyPair.getPrivate();
////        // Converting byte[] to Hex
//////        String publicKeyHex = convertBytesToHex(publicKey.getEncoded());
//////        String privateKeyHex = convertBytesToHex(privateKey.getEncoded());
////        // Trying to convert Hex to PublicKey/PrivateKey objects
////        PublicKey pkReconstructed = getPublicKey(publicKey.getEncoded());
////        PrivateKey skReconstructed = getPrivateKey(privateKey.getEncoded());
////        // This throws an error when running on an android device
////        // because there seems to be some library mismatch with
////        // java.security.* vs conscrypt.OpenSSL.* on android.
//
//
////        org.bouncycastle.jce.spec.ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
//////        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(QSystemConstants.END_CERT_EC_NAME_CURVE);
////        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
////        keyPairGenerator.initialize(ecSpec, new SecureRandom());
////        KeyPair ecKeyPair = keyPairGenerator.generateKeyPair();
////
////        ECPrivateKey privateKey = (ECPrivateKey) ecKeyPair.getPrivate();
////        ECPublicKey publicKey = (ECPublicKey) ecKeyPair.getPublic();
////
////        byte[] privateBytes = privateKey.getEncoded();
////        byte[] publicBytes = publicKey.getEncoded();
////
////        ECPublicKey bPublicKey = QSecurityUtils.ecPublicKeyFromBytes("secp256r1", publicBytes);
//////        System.out.println(ECUtil.publicKeyToString(publicKey));
////        System.out.println("");
//
//
////        AsymmetricCipherKeyPair keyPair = createEd25519KeyPair();
////
////        byte[] privateKeyBytes = getEd25519PrivateKeyToBytes(keyPair);
////        AsymmetricKeyParameter privateKey = getEd25519PrivateKeyFromBytes(privateKeyBytes);
////        String privateKeyPEM = getEd25519PrivateKeyPEM(keyPair);
////        System.out.println(privateKeyPEM);
////
////        byte[] publicKeyBytes = getEd25519PublicKeyToBytes(keyPair);
////        AsymmetricKeyParameter publicKey = getEd25519PublicKeyFromBytes(publicKeyBytes);
////        String publicKeyPEM = getEd25519PublicKeyPEM(keyPair);
////        System.out.println(publicKeyPEM);
//    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
} //////// End Class ///////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
