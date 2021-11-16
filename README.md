# png-reader-jdk8
Copy and paste from PNGImageReader from JDK8.

This aims to replace the default PNG reader on java 11 because of 26% more exceptions (like javax.imageio.IIOException: Invalid chunk length X) with partial images previously read fine with java 8.
