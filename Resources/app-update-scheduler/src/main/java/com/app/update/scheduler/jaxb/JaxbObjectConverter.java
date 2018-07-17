package com.app.update.scheduler.jaxb;

import java.io.StringReader;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;

public class JaxbObjectConverter {
    
    public static <T> T unmarshall(Class<T> clazz, String xml) throws JAXBException {
		return (T) JAXB.unmarshal(new StringReader(xml), clazz);
	}
}
