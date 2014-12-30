package com.vzome.core.zomic;



/**
public class ZomicException extends Exception{

	private  Exception m_culprit;

	public  ZomicException( Exception culprit ) {
		super( "wrapped" );

			m_culprit = culprit;
		}

	public  ZomicException( String msg ) {
		super( msg );
	}

	/**
	public  Exception getCulprit() {
		if ( m_culprit == null ) {
			return this;
		}
		if ( m_culprit instanceof ZomicException ) {
			return ((ZomicException) m_culprit) .getCulprit();
		}
		else {
			return m_culprit;
		}
	}


}

