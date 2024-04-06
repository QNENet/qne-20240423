package com.qnenet.qne.certificates;

import com.qnenet.qne.objects.classes.QNetMsgNewCertificate;
import com.qnenet.qne.system.constants.QSysConstants;
import com.qnenet.qne.system.impl.QNEPaths;
import com.qnenet.qne.system.utils.*;
import jakarta.annotation.PostConstruct;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

///////////////////////////////////////////////////////////////////////////////////////////////////
///////// Class ///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

@Component
@Scope("prototype")
public class QEndCertificatesServer {

//    Logger log = LoggerFactory.getLogger(QDiscoveryServerImpl.class);

    //    private static final int ENTRY_SIZE = 20;
    private static final int VALIDITY_YEARS = 1;
////    private final QObjects qobjs;
//
//    @Autowired
//    QSystem system;

    @Autowired
    QNEPaths qnePaths;

    //    @Autowired
//    QNEObjects qobjs;
//
//
//    private Path discoveryServerPath;
//    //    private Path certificateServerPath;
//    private Path nodeLookupRafFilePath;
//    private Path spareSlotsListFilePath;
//    private CopyOnWriteArrayList<Long> spareSlotsList;
//
//    private RandomAccessFile nodeLookupRaf;
    private PrivateKey interPrivateKey;
    private X509Certificate interCertificate;
    private PublicKey interPublicKey;
    private X509Certificate[] interCertificateChain;


///////////////////////////////////////////////////////////////////////////////////////////////////
///////// Activator ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    @PostConstruct
    public void init() {

        Path interKeyStorePath = Paths.get(qnePaths.getRepositoryPath().toString(), QSysConstants.INTER_KEYSTORE_DIR_NAME);
        Path interKeyStoreFilePath = Paths.get(interKeyStorePath.toString(), "interKeyStore.p12");

        JFrame jFrame = new JFrame();
        char[] interKeyStorePassPhrase = JOptionPane.showInputDialog(jFrame, "Enter Inter Keystore Pass Phrase").toCharArray();

        KeyStore interKeyStore = null;
        try {
            interKeyStore = QSecurityUtils.loadKeyStore(interKeyStoreFilePath, interKeyStorePassPhrase);

            ArrayList<String> allAliases = Collections.list(interKeyStore.aliases());
            ArrayList<String> interAliasses = new ArrayList<>();
            for (String alias : allAliases) {
                if (alias.startsWith(QSysConstants.INTERCERT_ALIAS_PREFIX)) {
                    interAliasses.add(alias);
                }
            }
            Collections.sort(interAliasses);

            int idx = QRandomUtils.randomIntBetween(0, interAliasses.size() - 1);

            String useInterAlias = interAliasses.get(idx);
            KeyStore.PrivateKeyEntry interEntry = (KeyStore.PrivateKeyEntry) interKeyStore.getEntry(useInterAlias,
                    new KeyStore.PasswordProtection(interKeyStorePassPhrase));

            interCertificate = (X509Certificate) interEntry.getCertificate();
            interCertificateChain = (X509Certificate[]) interEntry.getCertificateChain();
            interPrivateKey = interEntry.getPrivateKey();
            interPublicKey = interCertificate.getPublicKey();

//        networkStructureFilePath = Paths.get(discoveryServerPath.toString(), "networkStructure");
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableEntryException e) {
            throw new RuntimeException(e);
        }

//        system.registerObject(QSysConstants.DISCOVERY_SERVER, this);

    }

    public void getCertificate(QNetMsgNewCertificate netMsg) {
        QNetMsgNewCertificate nm = (QNetMsgNewCertificate) netMsg;

        String subject = "CN=QNE-NODE-ID : " + QBase36.qneIdToQNEIdString(nm.nodeId);
//        String subject = "CN=" + publicKnownInfo.netAddressPair.wanNetAddress.ipAddress;
        X500Name endCertSubject = new X500Name(subject);

        LocalDate localNow = LocalDate.now();
        LocalDate localNowMinus = localNow.minus((long) 7, ChronoUnit.DAYS);

        Date dateFrom = Date.from(localNowMinus.atStartOfDay().toInstant(ZoneOffset.UTC));
        Date dateTo = QDateTimeUtils.nowUTCPlusYears(VALIDITY_YEARS);

        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

//        PublicKey endPublicKey = QSecurityUtils.ecPublicKeyFromBytes(QSysConstants.END_CERT_EC_NAME_CURVE, sslPublicKeyPEM);
//        PublicKey endPublicKey = QSecurityUtils.ecPublicKeyFromPEM(sslPublicKeyPEM);

        try {

//            Reader rdr = new StringReader(publicKeyPEM); // or from file etc.
//            org.bouncycastle.util.io.pem.PemObject spki = new org.bouncycastle.util.io.pem.PemReader(rdr).readPemObject();
//            PublicKey endPublicKey = null;
//            endPublicKey = KeyFactory.getInstance("EC", "BC").generatePublic(new X509EncodedKeySpec(spki.getContent()));

            PublicKey endPublicKey = QSecurityUtils.ecdsaPublicKeyFromBytes(nm.sslPublicKeyEncoded);
            ContentSigner endCertContentSigner = new JcaContentSignerBuilder(
                    QSysConstants.END_CERT_SIGNATURE_ALGORITHM)
                    .setProvider(QSysConstants.END_CERT_BC_PROVIDER)
                    .build(interPrivateKey);

            X509v3CertificateBuilder endCertBuilder = new JcaX509v3CertificateBuilder(
                    interCertificate,
                    serialNumber,
                    dateFrom,
                    dateTo,
                    endCertSubject,
                    endPublicKey);

//        X509v3CertificateBuilder certBuilderV3 = new JcaX509v3CertificateBuilder(
//                interCertificate,
//                BigInteger.valueOf(System.currentTimeMillis()),
//                dateFrom,
//                dateTo,
//                new X500Name(subject),
//                endPublicKey);


////        String issuer = interCertificate.getSubjectX500Principal().toString();
//        String subject = "CN=QNE-Id -> " + QBase36.qneIdToFormattedQNEIdString(knownInfo.qneId);
//
//        LocalDate localNow = LocalDate.now();
//        LocalDate localNowMinus = localNow.minus((long) 7, ChronoUnit.DAYS);
//
//        Date dateFrom = Date.from(localNowMinus.atStartOfDay().toInstant(ZoneOffset.UTC));
//        Date dateTo = QDateTime.nowUTCPlusYears(VALIDITY_YEARS);
//
//        PublicKey endPublicKey = QSecurityUtils.publicKeyFromBytes(knownInfo.publicKeyBytes, null);
//
//        X509v3CertificateBuilder certBuilderV3 = new JcaX509v3CertificateBuilder(
//                interCertificate,
//                BigInteger.valueOf(System.currentTimeMillis()),
//                dateFrom,
//                dateTo,
//                new X500Name(subject),
//                endPublicKey);

///////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Extensions ////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

            JcaX509ExtensionUtils extUtils = null;
            extUtils = new JcaX509ExtensionUtils();

            // is CA certificate
            // certBuilderV3.addExtension(Extension.basicConstraints, true, new BasicConstraints(0));

            endCertBuilder.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(interPublicKey));

            endCertBuilder.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(endPublicKey));

///////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Extensions Alt Names //////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

//        certBuilderV3.addExtension(Extension.subjectAlternativeName,
//                false,
//                extUtils.createAuthorityKeyIdentifier(interPublicKey));


            ArrayList<GeneralName> altNames = new ArrayList<>();
            String wanIPAddress = nm.newNodeNetAddressPair.getWanIPAddress();
            String lanIPAddress = nm.newNodeNetAddressPair.getLanIPAddress();
            altNames.add(new GeneralName(GeneralName.iPAddress, wanIPAddress));
            if (nm.newNodeNetAddressPair.getWanIPAddress() != null) {
                altNames.add(new GeneralName(GeneralName.iPAddress, lanIPAddress));
            }

            GeneralNames subjectAltNames = GeneralNames.getInstance(new DERSequence((GeneralName[]) altNames.toArray(new GeneralName[]{})));
            endCertBuilder.addExtension(Extension.subjectAlternativeName, false, subjectAltNames);


///////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Certificate ///////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

            X509CertificateHolder endCertHolder = endCertBuilder.build(endCertContentSigner);
            X509Certificate endCert = new JcaX509CertificateConverter().setProvider(
                    QSysConstants.END_CERT_BC_PROVIDER).getCertificate(endCertHolder);

//            X509CertificateHolder certHldr = endCertBuilder.build(
//                    new JcaContentSignerBuilder("sha256WithRSAEncryption")
//                            .setProvider("BC")
//                            .build(interPrivateKey));
//
//            X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHldr);

//        X509CertificateHolder certHldr = certBuilderV3.build(new JcaContentSignerBuilder("SHA1WithRSA").setProvider("BC").build(interPrivateKey));
//
//        X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHldr);

///////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Verify  ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

            endCert.checkValidity(new Date());

            endCert.verify(interPublicKey);

///////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Bag Attribute  ////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

            PKCS12BagAttributeCarrier bagAttr = (PKCS12BagAttributeCarrier) endCert;

            // this is actually optional - but if you want to have control over setting the friendly name this is the way to do it...
            bagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, new DERBMPString("QNE End Certificate"));

            // this is also optional - in the sense that if you leave this out the keystore will add it automatically,
            // note though that for the browser to recognise the associated private key this you should at least use the
            // pkcs_9_localKeyId OID and set it to the same as you do for the private key's localKeyId.

            bagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, extUtils.createSubjectKeyIdentifier(endPublicKey));

///////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Finish  ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
            X509Certificate[] newChain = new X509Certificate[]{endCert, interCertificateChain[0], interCertificateChain[1]};

            nm.endCertificateBytes = QSecurityUtils.certificateArrayToByteArray(newChain);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (CertificateNotYetValidException e) {
            throw new RuntimeException(e);
        } catch (CertificateExpiredException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (CertIOException e) {
            throw new RuntimeException(e);
        } catch (OperatorCreationException e) {
            throw new RuntimeException(e);
        // Remove the catch block for IOException
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
} ///////// End Class /////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


