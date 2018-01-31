package biz.uro.CSVDataConverter.database.element;

public abstract class ElementValue {

	public static ElementValue NULL = new ElementValue() {
		@Override
		public String getProgram() {
			return "null";
		}		
	};
	
	public static ElementValue OBJECT( Object object ) {
		return new ElementValue( object ) {
			@Override
			public String getProgram() {
				return object.toString();
			}
		};
	}
	
	private final Object mValue;
	//private final String mProgram;
	private final String[] mUseTokens;
	
	private ElementValue() {
		mValue = null;
		mUseTokens = new String[0];
	}

	public ElementValue( Object value, String... useTokens ) {
		mValue = value;
		//mProgram = program;
		mUseTokens = useTokens;
	}
	
	public Object getValue() {
		return mValue;
	}
	
//	public String getProgram() {
//		return mProgram;
//	}
	
	public abstract String getProgram();
	
	public int getUseTokensCount() {
		return mUseTokens.length;
	}
	
	public String getUseToken( int index ) {
		return mUseTokens[index];
	}
	
	public String[] getUseTokens() {
		return mUseTokens;
	}
	
	@Override
	public boolean equals( Object other ) {
		if ( !( other instanceof ElementValue ) ) {
			return false;
		}
		final ElementValue otherValue = (ElementValue) other;
		if ( !mValue.equals( otherValue.mValue ) ) {
			return false;
		}
//		if ( !mProgram.equals( otherValue.mProgram ) ) {
//			return false;
//		}
		return true;
	}

}
