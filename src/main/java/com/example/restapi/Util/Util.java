package com.example.restapi.Util;

import com.example.restapi.Exception.AuthenticationException;
import com.example.restapi.Model.MemberModel;
import com.example.restapi.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Util {

    public String aesEncrypt(String strToEncrypt){
        String secret = "12345";

        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }

        return null;
    }

    public String decrypt(String strToDecrypt) {
        String secret = "12345";

        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }

        return null;
    }

    private static SecretKeySpec secretKey;
    private static byte[] key;

    private static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public boolean sessionValidation(MemberRepository memberRepositoy, String session, Long id, String service)throws AuthenticationException {
        boolean isValid = false;
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

        String sessionDecrypt = decrypt(session);
        String stringSplit[] = sessionDecrypt.split("::");
        Long idFromSession = Long.parseLong(stringSplit[0]);

        MemberModel userLogin = memberRepositoy.findById(idFromSession)
                .orElseThrow(() -> new AuthenticationException("Authentication Failed"));

        try {
            Date parsedDate  = dateFormat.parse(stringSplit[1]);
            Timestamp sessionTime = new Timestamp(parsedDate.getTime());

            long seconds = TimeUnit.MILLISECONDS.toSeconds(currentTime.getTime() - sessionTime.getTime());

            if(seconds <= 300 ) {
                if(service == "getUser"){
                    if(idFromSession != id) throw new AuthenticationException("Authentication Failed");
                }

                isValid = true;
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        return isValid;
    }
}

