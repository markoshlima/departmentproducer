package com.kafka.cripto.producer.department.service;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CommitmentPolicy;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class KmsService {

    @Autowired
    private KmsMasterKeyProvider keyProviderFinance;

    @Autowired
    private KmsMasterKeyProvider keyProviderSales;

    /**
     * Encripty with KMS master key ARN by department
     * @param data
     * @return base64 string
     */
    public String encryptData(final String data, final String department) {

        KmsMasterKeyProvider keyProvider = department.equals("finance") ? keyProviderFinance : keyProviderSales;

        AwsCrypto crypto = AwsCrypto.builder()
                .withCommitmentPolicy(CommitmentPolicy.RequireEncryptRequireDecrypt)
                .build();

        CryptoResult<byte[], KmsMasterKey> encryptResult = crypto.encryptData(keyProvider,
                data.getBytes(StandardCharsets.UTF_8));
        byte[] ciphertext = encryptResult.getResult();

        return new String(Base64.getEncoder().encode(ciphertext));

    }

}