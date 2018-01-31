package biz.uro.CSVDataConverter.database.element;

public class ElementException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ElementException( Object object ) {
		super( object.getClass().getSimpleName() );
	}
	
	public static class NullElementException extends ElementException {
		public NullElementException( Object object ) {
			super( object );
		}

		private static final long serialVersionUID = 1L;
	}
	
	public static class UnbreakableElementException extends ElementException {
		public UnbreakableElementException( Object object ) {
			super( object );
		}

		private static final long serialVersionUID = 1L;
	}
	
}
