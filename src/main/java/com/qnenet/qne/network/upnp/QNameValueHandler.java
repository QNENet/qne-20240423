/* 
 *              weupnp - Trivial upnp java library 
 *
 * Copyright (C) 2008 Alessandro Bahgat Shehata, Daniele Castagna
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Alessandro Bahgat Shehata - ale dot bahgat at gmail dot com
 * Daniele Castagna - daniele dot castagna at gmail dot com
 * 
 */

package com.qnenet.qne.network.upnp;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Map;

public class QNameValueHandler extends DefaultHandler {

    private Map<String,String> nameValue;

    private String currentElement;

    public QNameValueHandler(Map<String,String> nameValue) {
        this.nameValue = nameValue;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        currentElement = localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        currentElement = null;
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (currentElement != null) {
            String value = new String(ch,start,length);
            String old = nameValue.put(currentElement, value);
            if (old != null) {
                nameValue.put(currentElement, old + value);
            }
        }
    }

}
