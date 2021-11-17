# png-reader
Copy and paste of PNGImageReader from JDK11 changed to handle corrupted PNGs as JDK8 did

This aims to replace the default PNG reader on java 11 because of 26% more exceptions (like javax.imageio.IIOException: Invalid chunk length X) with partial images previously read fine with java 8.
