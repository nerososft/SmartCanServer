/*
 * Generated by ASN.1 Java Compiler (https://www.asnlab.org/)
 * From ASN.1 module "Platoon"
 */
package com.iot.nero.smartcan.entity.platoon;

import java.io.*;
import java.math.*;
import javax.validation.constraints.*;
import org.asnlab.asndt.runtime.conv.*;
import org.asnlab.asndt.runtime.conv.annotation.*;
import org.asnlab.asndt.runtime.type.AsnType;
import org.asnlab.asndt.runtime.value.*;

public class SmartTeamRequestMessage {

	@NotNull
	@Component(0)
	public Long msgCount;

	@NotNull
	@Component(1)
	public byte[] token;

	@NotNull
	@Size(min=8, max=8)
	@Component(2)
	public byte[] vid;

	@NotNull
	@Component(3)
	public ISFLEET isfleet;

	@NotNull
	@Size(min=8, max=8)
	@Component(4)
	public byte[] id;

	@NotNull
	@Component(5)
	public ROLE role;

	@NotNull
	@Component(6)
	public Long vehiclenum;

	@NotNull
	@Component(7)
	public Double frontdistance;

	@NotNull
	@Component(8)
	public Double speed;

	@NotNull
	@Component(9)
	public byte[] timestamp;

	@NotNull
	@Component(10)
	public Long syncNum;


	public Object clone() {
		return TYPE.clone(this, CONV);
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof SmartTeamRequestMessage)){
			return false;
		}
		return TYPE.equals(this, obj, CONV);
	}

	public void print(PrintStream out) {
		TYPE.print(this, CONV, out);
	}

	public void ber_encode(OutputStream out) throws IOException {
		TYPE.encode(this, EncodingRules.BASIC_ENCODING_RULES, CONV, out);
	}

	public static SmartTeamRequestMessage ber_decode(InputStream in) throws IOException {
		return (SmartTeamRequestMessage)TYPE.decode(in, EncodingRules.BASIC_ENCODING_RULES, CONV);
	}

	public void cer_encode(OutputStream out) throws IOException {
		TYPE.encode(this, EncodingRules.CANONICAL_ENCODING_RULES, CONV, out);
	}

	public static SmartTeamRequestMessage cer_decode(InputStream in) throws IOException {
		return (SmartTeamRequestMessage)TYPE.decode(in, EncodingRules.CANONICAL_ENCODING_RULES, CONV);
	}

	public void der_encode(OutputStream out) throws IOException {
		TYPE.encode(this, EncodingRules.DISTINGUISHED_ENCODING_RULES, CONV, out);
	}

	public static SmartTeamRequestMessage der_decode(InputStream in) throws IOException {
		return (SmartTeamRequestMessage)TYPE.decode(in, EncodingRules.DISTINGUISHED_ENCODING_RULES, CONV);
	}

	public void per_encode(boolean align, OutputStream out) throws IOException {
		TYPE.encode(this, align? EncodingRules.ALIGNED_PACKED_ENCODING_RULES:EncodingRules.UNALIGNED_PACKED_ENCODING_RULES, CONV, out);
	}

	public static SmartTeamRequestMessage per_decode(boolean align, InputStream in) throws IOException {
		return (SmartTeamRequestMessage)TYPE.decode(in, align? EncodingRules.ALIGNED_PACKED_ENCODING_RULES:EncodingRules.UNALIGNED_PACKED_ENCODING_RULES, CONV);
	}


	public final static AsnType TYPE = Platoon.type(65611);

	public final static CompositeConverter CONV;

	static {
		CONV = new AnnotationCompositeConverter(SmartTeamRequestMessage.class);
		AsnConverter msgCountConverter = LongConverter.INSTANCE;
		AsnConverter tokenConverter = Token.CONV;
		AsnConverter vidConverter = OctetStringConverter.INSTANCE;
		AsnConverter isfleetConverter = ISFLEET.CONV;
		AsnConverter idConverter = OctetStringConverter.INSTANCE;
		AsnConverter roleConverter = ROLE.CONV;
		AsnConverter vehiclenumConverter = LongConverter.INSTANCE;
		AsnConverter frontdistanceConverter = DoubleConverter.INSTANCE;
		AsnConverter speedConverter = DoubleConverter.INSTANCE;
		AsnConverter timestampConverter = TimeStamp.CONV;
		AsnConverter syncNumConverter = LongConverter.INSTANCE;
		CONV.setComponentConverters(new AsnConverter[] { msgCountConverter, tokenConverter, vidConverter, isfleetConverter, idConverter, roleConverter, vehiclenumConverter, frontdistanceConverter, speedConverter, timestampConverter, syncNumConverter });
	}


}
