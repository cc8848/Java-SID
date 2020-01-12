package com.quaint.demo.wechat.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * ip工具类
 * @author quaint
 * @date 2020-01-12 16:21:52
 */
@Slf4j
public abstract class IpUtils {

	/**
	 * 获取机器ip
	 * @return
	 */
	public static String getMachineIP() { 
	    try {
			InetAddress localHost = InetAddress.getLocalHost();
			if(localHost != null && localHost instanceof Inet4Address && !localHost.getHostAddress().equals("127.0.0.1")){
				return localHost.getHostAddress();
			}

	        /*
	         * Above method often returns "127.0.0.1", In this case we need to
	         * check all the available network interfaces
	         */
	        Enumeration<NetworkInterface> nInterfaces = NetworkInterface
	                .getNetworkInterfaces();
	        while (nInterfaces.hasMoreElements()) {
	            Enumeration<InetAddress> inetAddresses = nInterfaces
	                    .nextElement().getInetAddresses();
	            while (inetAddresses.hasMoreElements()) {
					InetAddress inetAddress = inetAddresses.nextElement();
					if(inetAddress != null && inetAddress instanceof Inet4Address && !inetAddress.getHostAddress().equals("127.0.0.1")){
						return inetAddress.getHostAddress();
					}
	            }
	        }
	    } catch (Exception e) {
	    	log.error(e.getMessage(),e);
	    }

	    return null;
	}
}
