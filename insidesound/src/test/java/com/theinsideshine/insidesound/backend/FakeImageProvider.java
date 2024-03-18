package com.theinsideshine.insidesound.backend;

public class FakeImageProvider {

    public static byte[] getFakeImageBytes() {
        // Bytes ficticios de una imagen JPEG
        byte[] fakeImageBytes = new byte[] {
                (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10, 'J', 'F', 'I', 'F', 0x00, 0x01, 0x01, 0x00, 0x00, 0x01,
                0x00, 0x01, 0x00, 0x00, (byte) 0xFF, (byte) 0xDB, 0x00, (byte) 0x84, 0x00, 0x08, 0x06, 0x06, 0x07, 0x06, 0x05, 0x08,
                // Más bytes de la imagen JPEG aquí...
        };
        return fakeImageBytes;
    }
}
