package biz.uro.CSVDataConverter.generator.table;

import java.util.ArrayList;
import java.util.List;

public class Test {
	
	private static final List<Test> PRIVATE_LIST = new ArrayList<>();
	private static boolean IS_LOADED;
	
	public static Test[] LIST;
	
	public static Test get( int a ) {
		for ( Test data : LIST ) {
			if ( data.a == a ) {
				return data;
			}
		}
		if ( !IS_LOADED ) {
			final Test data = new Test( a );
			PRIVATE_LIST.add( data );
			return data;
		}
		return null;
	}
	
	static void preLoad() {
		PRIVATE_LIST.clear();
		LIST = null;
		IS_LOADED = false;
	}

	static void postLoad() {
		LIST = PRIVATE_LIST.toArray( new Test[PRIVATE_LIST.size()] );
		IS_LOADED = true;
	}
	
	private final int a;

	private String b;
	private Test c;

	Test( int a ) {
		this.a = a;
	}

	void init( String b, Test c ) {
		this.b = b;
		this.c = c;
	}
	
	public int getA() {
		return this.a;
	}
	
	public String getB() {
		return this.b;
	}
	
	public Test getC() {
		return this.c;
	}
	
}
