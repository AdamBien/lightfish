#!/bin/bash
keytool -selfcert -dname "cn=Adam Bien, ou=Fun, o=adam-bien.com, c=DE" -alias lightview -keypass lightview -keystore ~/work/devprogs/tools/keystore/samples -storepass samples -validity 365