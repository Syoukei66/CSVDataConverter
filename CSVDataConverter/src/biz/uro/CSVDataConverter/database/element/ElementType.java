package biz.uro.CSVDataConverter.database.element;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import biz.uro.CSVDataConverter.swing.builder.ElementTypeBuilder;
import biz.uro.CSVDataConverter.swing.json.IJSONObject;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

public class ElementType {
	
	public enum Tag implements IJSONObject {
		PRIMITIVE	( false ),
		OBJECT		( false ),
		TABLE		( false ),
		FACTORY		( false ),
		USER		( true ),
		;
		public static final Tag[] VALUES = values();
		final boolean mIsEditable;
		Tag( boolean isEditable ) {
			mIsEditable = isEditable;
		}
		public boolean isEditable() {
			return mIsEditable;
		}
		@Override
		public void jsonMapping(JSONWriter writer) {
			
		}
		@Override
		public String jsonHashCode() {
			return "Tag_" + StringUtils.capitalize( StringUtils.lowerCase( name() ) );
		}
	}
	
	private final Tag mTag;
	private final String mTypeName;
	private final String mWrapperName;
	private final String mPackageName;
	private final boolean mIsTerminal;

	public ElementType( ElementTypeBuilder builder ) {
		this( builder, null );
	}
	
	public ElementType( ElementTypeBuilder builder, String wrapperName ) {
		mTag = builder.getTag();
		mTypeName = builder.getPackage().getClassName();
		mWrapperName = wrapperName;
		mPackageName = builder.getPackage().getPackage();
		mIsTerminal = builder.isTerminal();
	}

	public Tag getTag() {
		return mTag;
	}

	public String getTypeName() {
		return mTypeName;
	}
	
	public String getWrapperName() {
		return mWrapperName;
	}
	
	public String getPackageName() {
		return mPackageName;
	}
	
	public boolean isTerminal() {
		return mIsTerminal;
	}

	@Override
	public int hashCode() {
		return Objects.hash( mTag, mTypeName, mWrapperName, mPackageName, mIsTerminal );
	};
	
	@Override
	public boolean equals( Object other ) {
		if ( !( other instanceof ElementType ) ) {
			return false;
		}
		final ElementType otherElement = ( ElementType )other;
		if ( mTag != null && !mTag.equals( otherElement.mTag ) ) {
			return false;
		}
		if ( mTypeName != null && !mTypeName.equals( otherElement.mTypeName ) ) {
			return false;
		}
		if ( mWrapperName != null && !mWrapperName.equals( otherElement.mWrapperName ) ) {
			return false;
		}
		if ( mPackageName != null && !mPackageName.equals( otherElement.mPackageName ) ) {
			return false;
		}
		if ( mIsTerminal != otherElement.mIsTerminal ) {
			return false;
		}
		return true;
	}
	
}
