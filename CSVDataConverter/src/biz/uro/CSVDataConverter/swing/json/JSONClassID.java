package biz.uro.CSVDataConverter.swing.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {
	ElementType.TYPE
} )
@Retention( RetentionPolicy.RUNTIME )
public @interface JSONClassID {
	String[] value();
}
