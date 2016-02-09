package edu.stanford.bmir.protege.web.client.rpc.data.layout;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

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

	public Optional<Integer> getIndexAsInt() {
	    if (index == null) {
	        return Optional.absent();
	    }
	    try {
	       return Optional.of(Integer.valueOf(index));
	    } catch (NumberFormatException e) {
	        return Optional.absent();
	    }
	}

	public void setIndex(int index) {
        this.index = String.valueOf(index);
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


	@Override
	public String toString() {
		return toStringHelper("PortletConfiguration")
				.add("name", name)
				.add("height", height)
				.add("width", width)
				.add("index", index)
				.toString();
	}
}
