package com.deveficiente.pagamentos.pagamentoonline;

import java.util.Objects;
import java.util.stream.Stream;

public class ForceSiteCall {

	public static void fromExactlyPoint(Class<?> originalSiteCall) {
		Objects.requireNonNull(originalSiteCall);
		//[0] -> getStackTrace()
		//[1] -> ForceSiteCall.from
		//[2] -> method where ForceSiteCall is called
		//[3] -> Original point where you want to force the coupling
		
		StackTraceElement classSiteCallElement = Thread.currentThread()
				.getStackTrace()[3];
		if(!originalSiteCall.getName().equals(classSiteCallElement.getClassName())) {
			System.err.println("The exactly call site that you were expecting  was ["+originalSiteCall+"] but the currently one being used is "+classSiteCallElement);
			throw new IllegalStateException("The original call site that you were expecting  was ["+originalSiteCall+"] but the currently one being used is "+classSiteCallElement);
		}
	}
	
	public static void fromOriginalPoint(Class<?> originalSiteCall) {
		Objects.requireNonNull(originalSiteCall);
		StackTraceElement[] traceElements = Thread.currentThread()
				.getStackTrace();
		
		boolean foundOriginalPoint = Stream.of(traceElements).anyMatch(traceElement -> {
			return originalSiteCall.getName().equals(traceElement.getClassName());
		});
		
		if(!foundOriginalPoint) {
			System.err.println("The original call site that you were expecting  was ["+originalSiteCall+"]");
			throw new IllegalStateException("The original call site that you were expecting  was ["+originalSiteCall+"]");
		}
	}

}
