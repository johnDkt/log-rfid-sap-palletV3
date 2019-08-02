package com.decathlon.log.rfid.pallet.utils;

import org.apache.log4j.Logger;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkInterfaceHelper {

    private org.apache.log4j.Logger LOGGER = Logger.getLogger(NetworkInterfaceHelper.class.getName());

    public String getIpFromNetworkInterfaces() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface currentInterface = interfaces.nextElement();
                if (!currentInterface.isLoopback()
                        && currentInterface.isUp()
                        && (currentInterface.getName().startsWith("eth") || currentInterface.getName().startsWith("wlan") || currentInterface
                        .getName().startsWith("net"))) {
                    return getIPFromNetworkInterface(currentInterface);
                }
            }
        } catch (SocketException e) {
            LOGGER.info("Erreur lors de la récupération des IP ", e);
        }
        return "!error-nointerfacefound!";

    }

    private String getIPFromNetworkInterface(NetworkInterface currentInterface) {
        // Retourne la première ip de la liste des InetAddresses
        Enumeration<InetAddress> ips = currentInterface.getInetAddresses();
        while (ips.hasMoreElements()) {
            InetAddress ip = ips.nextElement();
            if (ip instanceof Inet4Address) {
                return ip.getHostAddress();
            }
        }
        return "!error-noipfound!";

    }
}
