package com.qnenet.qne.cacertificates;

//import com.qnenet.qne.qneapi.constants.QSysConstants;
//import com.qnenet.qne.qneutils.QDateTime;
//import com.qnenet.qne.qneutils.QSecurityUtils;
//import com.qnenet.qne.qsystem.appendStore.QSystem;

import com.qnenet.qne.system.constants.QSysConstants;
import com.qnenet.qne.system.impl.QNEPaths;
import com.qnenet.qne.system.utils.QDateTimeUtils;
import com.qnenet.qne.system.utils.QSecurityUtils;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

/*
 * Note :
 * PKCS12 keystore requires keystore Pwd and
 * private key password to be the same
 * */

@Component
@Scope("prototype")
public class QRootCertificate {

    //    private static final int KEY_SIZE = 4096;
    private static final int VALIDITY_YEARS = 50;

//    static Logger log = LoggerFactory.getLogger(QRootCertificate.class);

////////////  Custom  //////////////////////////////////////////////////////////////////////////////////////////////////

//    @Autowired
//    QSystem system;

    //    @Autowired
    QNEPaths qnePaths;

    //    private char[] rootPrivateKeyPassPhraseChars;
    private KeyStore rootKeyStore;
    //    private Path keystoreFilePath;
//    private char[] keyStorePwd;
//    private char[] keyPwd;
    private Path rootKeyStoreFilePath;
    private char[] rootKeyStorePassPhrase;
    private String nameCurve = "P-384";
    private String keyAlgo = "ECDSA";
    private String signatureAlgo = "SHA256withECDSA";
    private String bcProvider = "BC";


///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Activate //////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


    public QRootCertificate(QNEPaths qnePaths) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, UnrecoverableEntryException, InvalidAlgorithmParameterException, SignatureException, OperatorCreationException, InvalidKeyException {
//    }
//
//    //    @Activate
//    public void activate() throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException,
//            NoSuchProviderException, UnrecoverableEntryException, SignatureException,
//            OperatorCreationException, InvalidKeyException, InvalidAlgorithmParameterException {

        Path rootKeyStorePath = Paths.get(qnePaths.getRepositoryPath().toString(), QSysConstants.ROOT_KEYSTORE_DIR_NAME);
        rootKeyStoreFilePath = Paths.get(rootKeyStorePath.toString(), "rootKeyStore.p12");
        if (Files.exists(rootKeyStoreFilePath))
            return;
        /*
         * get caKeyStorePassPhrase from the user
         * */
        JFrame jFrame = new JFrame();
        rootKeyStorePassPhrase = JOptionPane.showInputDialog(jFrame, "Enter Root Keystore Pass Phrase").toCharArray();


        if (Files.notExists(rootKeyStorePath)) {
            Files.createDirectories(rootKeyStorePath);
            rootKeyStore = QSecurityUtils.createKeyStore();
            QSecurityUtils.saveKeyStore(rootKeyStoreFilePath, rootKeyStore, rootKeyStorePassPhrase);
        } else {
            rootKeyStore = QSecurityUtils.loadKeyStore(rootKeyStoreFilePath, rootKeyStorePassPhrase);
        }


//        Path etcPath = Paths.get(system.getKarafPath().toString(), "etc");
//        Path paxWebPath = Paths.get(etcPath.toString(), "org.ops4j.pax.web.cfg");
//        Properties paxWebCfgProps = QPropertyUtils.loadPropertiesFromFile(paxWebPath);
//        keystoreFilePath = Paths.get(paxWebCfgProps.getProperty("org.ops4j.pax.web.ssl.keystore"));
//        keyPwd = paxWebCfgProps.getProperty("org.ops4j.pax.web.ssl.keypassword").toCharArray();
//        keyStorePwd = paxWebCfgProps.getProperty("org.ops4j.pax.web.ssl.password").toCharArray();
//
//        keyStore = QSecurityUtils.loadKeyStore(keystoreFilePath, keyStorePwd);

        KeyStore.PrivateKeyEntry rootEntry = (KeyStore.PrivateKeyEntry) rootKeyStore.getEntry("qneRoot",
                new KeyStore.PasswordProtection(rootKeyStorePassPhrase));


        if (rootEntry == null) newSystem();

//        log.info("Hello from -> " + getClass().getSimpleName());
    }

    //    @Deactivate
    public void deactivate() {
        Arrays.fill(rootKeyStorePassPhrase, '0');
//        log.info("Goodbye from -> " + getClass().getSimpleName());
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// New System ////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    private void newSystem() throws KeyStoreException, CertificateException, IOException,
            NoSuchAlgorithmException, SignatureException, OperatorCreationException,
            InvalidKeyException, NoSuchProviderException, InvalidAlgorithmParameterException {

//        KeyPair rsaKeyPair = QSecurityUtils.getRSAKeyPair(KEY_SIZE);

        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(nameCurve);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyAlgo, "BC");
        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        KeyPair rootKeyPair = keyPairGenerator.generateKeyPair();


        X509Certificate certificate = createRootCert(rootKeyPair, VALIDITY_YEARS);

        Certificate[] certificateArray = new Certificate[]{certificate};

        rootKeyStore.setKeyEntry("qnerootca", rootKeyPair.getPrivate(), rootKeyStorePassPhrase, certificateArray);

        QSecurityUtils.saveKeyStore(rootKeyStoreFilePath, rootKeyStore, rootKeyStorePassPhrase);

    }

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Root Certificate //////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public X509Certificate createRootCert(KeyPair rootKeyPair, int validityYears) throws OperatorCreationException, CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException, CertIOException {
        PublicKey caPubKey = rootKeyPair.getPublic();
        // PrivateKey caPrivKey = rootKeyPair.getPrivate();

        String issuer = "C=AU, O=QNECommunity, OU=QNE Primary Certificate";
        X500Name issuerName = new X500Name(issuer);
        X500Name subjectName = new X500Name(issuer);
//        PublicKey subjectPubicKey = caPubKey;

        LocalDate localNow = LocalDate.now();
        LocalDate localNowMinus = localNow.minus((long) 7, ChronoUnit.DAYS);

        Date dateFrom = Date.from(localNowMinus.atStartOfDay().toInstant(ZoneOffset.UTC));
        Date dateTo = QDateTimeUtils.nowUTCPlusYears(VALIDITY_YEARS);

        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

        ContentSigner rootCertContentSigner = new JcaContentSignerBuilder(signatureAlgo).setProvider(bcProvider).build(rootKeyPair.getPrivate());
        X509v3CertificateBuilder rootCertBuilder = new JcaX509v3CertificateBuilder(issuerName, serialNumber, dateFrom, dateTo, subjectName, rootKeyPair.getPublic());

        // Add Extensions
        // A BasicConstraint to mark root certificate as CA certificate
        JcaX509ExtensionUtils rootCertExtUtils = new JcaX509ExtensionUtils();
        rootCertBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
        rootCertBuilder.addExtension(Extension.subjectKeyIdentifier, false, rootCertExtUtils.createSubjectKeyIdentifier(rootKeyPair.getPublic()));

        // Create a cert holder and export to X509Certificate
        X509CertificateHolder rootCertHolder = rootCertBuilder.build(rootCertContentSigner);
        X509Certificate rootCert = new JcaX509CertificateConverter().setProvider(bcProvider).getCertificate(rootCertHolder);


        rootCert.checkValidity(new Date());

        rootCert.verify(caPubKey);

        PKCS12BagAttributeCarrier bagAttr = (PKCS12BagAttributeCarrier) rootCert;

        // this is actually optional - but if you want to have control over setting the friendly name
        // this is the way to do it...
        bagAttr.setBagAttribute(
                PKCSObjectIdentifiers.pkcs_9_at_friendlyName,
                new DERBMPString("QNE Primary Certificate"));

        return rootCert;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
} /////// End Class ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
