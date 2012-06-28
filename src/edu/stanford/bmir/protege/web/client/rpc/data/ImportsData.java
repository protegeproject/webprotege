package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class ImportsData implements Serializable {
    private static final long serialVersionUID = 3114870825267420445L;

    private String name;
	private ArrayList<ImportsData> imports = new ArrayList<ImportsData>();

	public ImportsData() {
	}

	public ImportsData(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<ImportsData> getImports() {
		return imports;
	}

	public void setImports(ArrayList<ImportsData> imports) {
		this.imports = imports;
	}
	
	public void addImport(ImportsData data) {
		this.imports.add(data);
	}
}
