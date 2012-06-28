package edu.stanford.bmir.protege.web.client.rpc.data.layout;

import java.io.Serializable;
import java.util.List;

public class PortletConfiguration extends GenericConfiguration implements Serializable {

	private static final long serialVersionUID = -1067323872900631937L;

	private String name;
	private int height;
	private int width;
	private String index;
	private List<String> writeAccessGroups;

	public PortletConfiguration() {
		this.name = "unnamed";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getIndex() {
        return index;
    }

	public int getIndexAsInt() {
	    if (index == null) {
	        return 0;
	    }
	    try {
	       return Integer.valueOf(index);
	    } catch (NumberFormatException e) {
	        return 0;
	    }
	}

	public void setIndex(String index) {
        this.index = index;
    }

    public List<String> getWriteAccessGroups() {
        return writeAccessGroups;
    }

    public void setWriteAccessGroups(List<String> writeAccessGroups) {
        this.writeAccessGroups = writeAccessGroups;
    }

	@Override
	public boolean equals(Object obj) {
	    if (!(obj instanceof PortletConfiguration)) {
	        return false;
	    }
	   PortletConfiguration pc2 = (PortletConfiguration) obj;
	   boolean success = name.equals(pc2.getName());
	   if (pc2.getIndex() == null && index == null) {
	       return success;
	   }
	   if (index != null) {
	       success = success && index.equals(pc2.getIndex());
	   }
	   return success;
	}

	@Override
	public int hashCode() {
	    return name.hashCode() + (index != null ? index.hashCode() : 0);
	}

}
