public class RC4 {
        private final int[] S = new int[256];//2^8
        private final byte[] T = new byte[256];
        private final int keylength;
        private final int mod = 256;
        private int mod(int input)
        {
            return (input % mod);
        }
        private void swap(int[] mass, int index1, int index2)
        {
            int temp = mass[index1];
            mass[index1] = mass[index2];
            mass[index2] = temp;
        }

        public RC4(final byte[] key) {
            if (key.length < 1 || key.length > 256) {
                throw new IllegalArgumentException("key must be between 1 and 256 bytes");
            } else {
                keylength = key.length;
                for (int i = 0; i < 256; i++) {
                    S[i] = i;
                    T[i] = key[i % keylength]; //дублируем ключ, если он короче 256
                }
                int j = 0;
                for (int i = 0; i < 256; i++) {// key-scheduling algorithm
                    j = mod(j + S[i] + T[i]);
                    swap(S, i, j);
                }
            }
        }

        public byte[] encrypt(final byte[] plaintext) {
            final byte[] ciphertext = new byte[plaintext.length];
            int i = 0, j = 0, k, t;
            for (int counter = 0; counter < plaintext.length; counter++) {
                i = mod(i + 1);
                j = mod(j + S[i]);

                swap(S, i, j);
                t = mod(S[i] + S[j]);
                k = S[t];// key
                ciphertext[counter] = (byte) (plaintext[counter] ^ k);// msg xor key
            }
            return ciphertext;
        }

        public byte[] decrypt(final byte[] ciphertext) {
            return encrypt(ciphertext);
        }

}
