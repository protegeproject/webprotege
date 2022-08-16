package ch.unifr.digits.webprotege.attestation.server.compression.tree;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;

public class ObjectList {
    private ArrayList<String> list;


    public ObjectList(ArrayList<String> list) {
        this.list = list;
    }

    public ObjectList(String object) {
        ArrayList<String> object_list = new ArrayList<>();
        object_list.add(object);
        this.list = object_list;
    }

    public String getDigest() {
        StringBuilder sb = new StringBuilder();

        for (String element : list) {
            sb.append(element);
        }

        return DigestUtils.sha256Hex(sb.toString());
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public boolean contains(String object) {
        return list.contains(object);
    }

    private String getIndentedString(int nb_indents) {
        StringBuilder sb = new StringBuilder();

        sb.append("\t".repeat(Math.max(0, nb_indents)));

        return sb.toString();
    }

    public void print(int nb_indents) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(getIndentedString(nb_indents) + "Object " + i + ": " + list.get(i));
        }
    }
}
