package com.banking.core.infraestrutura.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/** Resuelve la dirección IP del host para los campos de auditoría. */
public final class ServerIpAddressResolver {

    private ServerIpAddressResolver() {
    }

    public static String resolve() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException exception) {
            return InetAddress.getLoopbackAddress().getHostAddress();
        }
    }
}
