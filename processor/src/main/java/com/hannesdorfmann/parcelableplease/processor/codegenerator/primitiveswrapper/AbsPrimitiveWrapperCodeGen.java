package com.hannesdorfmann.parcelableplease.processor.codegenerator.primitiveswrapper;

import com.hannesdorfmann.parcelableplease.processor.ParcelableField;
import com.hannesdorfmann.parcelableplease.processor.codegenerator.CodeGenerator;
import com.hannesdorfmann.parcelableplease.processor.codegenerator.FieldCodeGen;
import java.io.IOException;
import repacked.com.squareup.javawriter.JavaWriter;

import static com.hannesdorfmann.parcelableplease.processor.codegenerator.CodeGenerator.PARAM_PARCEL;
import static com.hannesdorfmann.parcelableplease.processor.codegenerator.CodeGenerator.PARAM_SOURCE;
import static com.hannesdorfmann.parcelableplease.processor.codegenerator.CodeGenerator.PARAM_TARGET;

/**
 * It also handles null values
 *
 * @author Hannes Dorfmann
 */
public class AbsPrimitiveWrapperCodeGen implements FieldCodeGen {

  private String methodSuffix;

  public AbsPrimitiveWrapperCodeGen(String methodSuffix) {
    this.methodSuffix = methodSuffix;
  }

  @Override public void generateWriteToParcel(ParcelableField field, JavaWriter jw)
      throws IOException {

    jw.emitStatement("%s.writeByte( (byte) (%s.%s != null ? 1 : 0) )", CodeGenerator.PARAM_PARCEL,
        CodeGenerator.PARAM_SOURCE, field.getFieldName());

    jw.beginControlFlow("if (%s.%s != null)", CodeGenerator.PARAM_SOURCE, field.getFieldName());
    jw.emitStatement("%s.write%s(%s.%s)", PARAM_PARCEL, methodSuffix, PARAM_SOURCE,
        field.getFieldName());
    jw.endControlFlow();
  }

  @Override public void generateReadFromParcel(ParcelableField field, JavaWriter jw)
      throws IOException {

    jw.emitStatement("boolean %sNullHelper", field.getFieldName());
    jw.emitStatement("%sNullHelper = ( %s.readByte() == 1 )", field.getFieldName(),
        CodeGenerator.PARAM_PARCEL);

    jw.beginControlFlow("if (%sNullHelper)", field.getFieldName());
    jw.emitStatement("%s.%s = %s.read%s()", PARAM_TARGET, field.getFieldName(), PARAM_PARCEL,
        methodSuffix);
    jw.nextControlFlow("else");
    jw.emitStatement("%s.%s = null", PARAM_TARGET, field.getFieldName());
    jw.endControlFlow();
  }
}
