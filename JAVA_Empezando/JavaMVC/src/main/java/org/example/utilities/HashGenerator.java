package org.example.utilities;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static org.example.DataAccess.services.AuthService.*;

public class HashGenerator {
    // Mismos valores que en AuthService
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    public static void main(String[] args) {


            System.out.println("========================================");
            System.out.println("  Hash Generator - Car Maintenance DB  ");
            System.out.println("========================================\n");

            // Generar hashes para los 3 usuarios del Main
            generateHash("johndoe", "pass123");
            generateHash("janedoe", "pass456");
            generateHash("admin", "admin123");

            System.out.println("\n========================================");
            System.out.println("INSTRUCCIONES:");
            System.out.println("1. Primero ejecuta los INSERT para crear los usuarios");
            System.out.println("2. Luego ejecuta los UPDATE de arriba para actualizar los hashes");
            System.out.println("========================================\n");

            System.out.println("-- PASO 1: Insertar usuarios (solo si no existen)");
            System.out.println("INSERT INTO users (username, email, password_hash, salt, role, created_at, updated_at)");
            System.out.println("VALUES");
            System.out.println("  ('johndoe', 'john@example.com', 'temp', 'temp', 'USER', NOW(), NOW()),");
            System.out.println("  ('janedoe', 'jane@example.com', 'temp', 'temp', 'USER', NOW(), NOW()),");
            System.out.println("  ('admin', 'admin@example.com', 'temp', 'temp', 'ADMIN', NOW(), NOW());");
            System.out.println();
        }

        private static void generateHash(String username, String password) {
            // Generar salt aleatoria (como lo hace AuthService)
            String salt = generateSalt();

            // Generar hash usando PBKDF2 (mismo algoritmo que AuthService)
            String hash = hashPassword(password, salt);

            System.out.println("-- Usuario: " + username + " | Contraseña: " + password);
            System.out.println("UPDATE users SET");
            System.out.println("  password_hash = '" + hash + "',");
            System.out.println("  salt = '" + salt + "'");
            System.out.println("WHERE username = '" + username + "';");
            System.out.println();
        }

        /**
         * Genera una salt aleatoria (mismo método que AuthService)
         */
        private static String generateSalt() {
            SecureRandom random = new SecureRandom();
            byte[] saltBytes = new byte[SALT_LENGTH];
            random.nextBytes(saltBytes);
            return Base64.getEncoder().encodeToString(saltBytes);
        }

        /**
         * Genera el hash de la contraseña usando PBKDF2WithHmacSHA256
         * (mismo método que AuthService)
         */
        private static String hashPassword(String password, String salt) {
            try {
                byte[] saltBytes = Base64.getDecoder().decode(salt);

                PBEKeySpec spec = new PBEKeySpec(
                        password.toCharArray(),
                        saltBytes,
                        ITERATIONS,
                        KEY_LENGTH
                );

                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                byte[] hash = factory.generateSecret(spec).getEncoded();

                return Base64.getEncoder().encodeToString(hash);

            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException("Error al generar hash: " + e.getMessage(), e);
            }
        }
    }

