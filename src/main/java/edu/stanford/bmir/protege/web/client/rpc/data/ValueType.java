package edu.stanford.bmir.protege.web.client.rpc.data;

@Deprecated
public enum ValueType { //TODO: fix cases - problem with mapping bw value type protege and value type webpro
	String,
	Symbol,
	Literal,
	Integer,
	Float,
	Date,
	Boolean,
	Instance, //TODO: hack change back
	Cls,
	Property,
	Any
}

//TODO: These are just random types, other ones can be added.
