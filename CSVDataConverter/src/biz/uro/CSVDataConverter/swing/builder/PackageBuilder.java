package biz.uro.CSVDataConverter.swing.builder;

import java.util.ArrayList;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import biz.uro.CSVDataConverter.database.element.ElementType.Tag;
import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.json.JSONObjectMap.IJSONObjectLoadedListener;
import biz.uro.CSVDataConverter.swing.json.JSONObjectMap.IJSONObjectReferedListener;

public class PackageBuilder implements IJSONDataModel, Comparable<PackageBuilder> {
	
	public interface IPackageChangeListener {
		void onChangePackage( PackageBuilder packaqe );
	}

	private static final String TAG_HASH_CODE	= "TagHashCode";
	private static final String PACKAGE			= "PackagePath";
	private static final String CLASS_NAME		= "ClassName";
	
	private final Tag mTag;
	private String mPackage;
	private String mClassName;
	
	private final ArrayList<IPackageChangeListener> mListener = new ArrayList<>();
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( TAG_HASH_CODE, mTag.jsonHashCode() );
		writer.add( PACKAGE, mPackage );
		writer.add( CLASS_NAME, mClassName );
	}
	
	public PackageBuilder( JSONReader reader ) {
		mTag = reader.get( TAG_HASH_CODE ).pullObject();
		mPackage = reader.get( PACKAGE ).asText();
		mClassName = reader.get( CLASS_NAME ).asText();
	}

	public PackageBuilder( Tag tag ) {
		mTag = tag;
	}
	
	public void setPackage( String packaqe ) {
		mPackage = packaqe;
		onChangePackage();
	}
	
	public String getPackage() {
		return mPackage;
	}
	
	public void setClassName( String className ) {
		mClassName = className;
		onChangePackage();
	}
	
	public String getClassName() {
		return mClassName;
	}

	@Override
	public boolean isEditable() {
		return true;
	}
	
	public String getPackagePath() {
		return mPackage + "." + mClassName;
	}
	
	public String[] getPackagePaths() {
		return StringUtils.split(getPackagePath(), ".");
	}
	
	public Tag getTag() {
		return mTag;
	}

	@Override
	public String toString() {
		return String.format( "%-48s %-16s", mPackage, mClassName );
	}

	@Override
	public String jsonHashCode() {
		return Integer.toString( hashCode() );
	}

	public void addPackageChangeListener( IPackageChangeListener listener ) {
		mListener.add( listener );
	}
	
	public void removePackageChangeListener( IPackageChangeListener listener ) {
		mListener.remove( listener );
	}
	
	public void clearPackageChangeListener() {
		mListener.clear();
	}
	
	public void onChangePackage() {
		for ( IPackageChangeListener listener : mListener ) {
			listener.onChangePackage( this );
		}
	}
	
	/* =============================================================================== 
	 * implements (Primitive)
	 * =============================================================================== */
	
	public static final PackageBuilder _int		= new _Primitive( "int" );
	public static final PackageBuilder _float	= new _Primitive( "float" );
	public static final PackageBuilder _double	= new _Primitive( "double" );
	public static final PackageBuilder _boolean	= new _Primitive( "boolean" );
	public static final PackageBuilder _String	= new _Terminal( "String" );

	public static final PackageBuilder[] TERMINALS = new PackageBuilder[] {
		_int,
		_float,
		_double,
		_boolean,
		_String,
	};
	
	private static class _Primitive extends _Terminal {
		public _Primitive( String typeName ) {
			super( typeName );
		}
	}
	
	private static class _Terminal extends PackageBuilder {
		public _Terminal( String typeName ) {
			super( Tag.PRIMITIVE );
			setClassName( typeName );
		}
		@Override
		public String jsonHashCode() {
			return "Package_" + StringUtils.capitalize( getClassName() );
		}
		@Override
		public boolean isEditable() {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash( mPackage, mClassName );
	};

	@Override
	public boolean equals( Object obj ) {
		if ( !(obj instanceof PackageBuilder) ) {
			return false;
		}
		final PackageBuilder other = (PackageBuilder)obj;
		if ( mPackage == null ) {
			if ( other.mPackage != null ) {
				return false;
			}
		}
		else if ( !mPackage.equals( other.mPackage ) ) {
			return false;
		}
		if ( mClassName == null ) {
			if ( other.mClassName != null ) {
				return false;
			}
		}
		else if ( !mClassName.equals( other.mClassName ) ) {
			return false;
		}
		return true;
	};
	
	@Override
	public int compareTo( PackageBuilder o ) {
		if ( o == null ) {
			return getPackagePath().compareTo( "" );
		}
		return getPackagePath().compareTo( o.getPackagePath() );
	}
	
}
