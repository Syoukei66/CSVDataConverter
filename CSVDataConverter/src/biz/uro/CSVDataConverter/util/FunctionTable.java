//package biz.uro.CSVDataConverter.util;
//
//import java.util.HashSet;
//import biz.uro.CSVDataConverter.database.Schema;
//import biz.uro.CSVDataConverter.database.element.Element;
//import biz.uro.CSVDataConverter.database.element.ElementParser;
//import biz.uro.CSVDataConverter.database.element.ElementType;
//
//public class FunctionTable {
//	
//	public interface IFunctionArgumentRegister {
//		String identifier();
//		String functionName();
//		Element.Base[] argments();
//	}
//	
//	public static class Builder {
//		private final String mPackagePath;
//		private final String mTypeName;
//		private final HashSet<String> mPackagePaths = new HashSet<>();
//		public Builder( String packagePath, String typeName ) {
//			mPackagePath = packagePath;
//			mTypeName = typeName;
//		}
//		
//		public Builder packagePath( String packagePath, String typeName ) {
//			mPackagePaths.add( packagePath + "." + typeName );
//			return this;
//		}
//		
//		public FunctionTable build() {
//			return new FunctionTable( this );
//		}
//	}
//	private final ElementType.Builder mTypeBuilder;
//	private boolean mIsLoaded;
//	
//	public FunctionTable( Builder builder ) {
//		mTypeBuilder = new ElementType.Builder( builder.mTypeName );
//		mTypeBuilder.addPackage( builder.mPackagePath + "." + builder.mTypeName );
//		for ( String packagePath : builder.mPackagePaths ) {
//			mTypeBuilder.addPackage( packagePath );
//		}
//	}
//	
//	public ElementType createType( IFunctionArgumentRegister[] registers ) {
//		if ( !mIsLoaded ) {
//			for ( IFunctionArgumentRegister register : registers ) {
//				final Schema schema = new Schema()
//				{{
//					final Element.Base[] elementBases = register.argments();
//					for ( int i = 0; i < elementBases.length; i++ ) {
//						final String name = "arg" + i;
//						element( name, elementBases[i] );							
//					}
//				}};
//				mTypeBuilder.addParser( new ElementParser._Function( register.identifier(), schema ) {
//					@Override
//					protected String parseFuncName( ElementType type, String simpleFuncName ) {
//						return register.functionName();
//					}
//				});
//			}
//			mIsLoaded = true;
//		}
//		return mTypeBuilder.build();
//	}
//	
//}
