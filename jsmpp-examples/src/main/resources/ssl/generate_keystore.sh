#!/bin/sh
keytool -genkey -keyalg RSA -alias selfsigned -storetype PKCS12 -keystore keystore.p12 -storepass password -validity 3650 -keysize 2048 -keypass password -dname "cn=jSMPP, ou=Development, o=Open Telecoms, c=US"
