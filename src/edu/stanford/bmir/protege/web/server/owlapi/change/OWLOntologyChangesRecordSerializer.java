package edu.stanford.bmir.protege.web.server.owlapi.change;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.coode.owlapi.owlxml.renderer.OWLXMLObjectRenderer;
import org.coode.owlapi.owlxml.renderer.OWLXMLWriter;
import org.coode.owlapi.owlxmlparser.OWLXMLParser;
import org.semanticweb.owlapi.binaryowl.OWLObjectBinaryType;
import org.semanticweb.owlapi.binaryowl.lookup.LookupTable;
import org.semanticweb.owlapi.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/04/2012
 */
public class OWLOntologyChangesRecordSerializer {


//    public static final int CHANGE_RECORD_MARKER = 33;
//
//    private OWLAPIProject project;
//
//    public OWLOntologyChangesRecordSerializer(OWLAPIProject project) {
//        this.project = project;
//    }
//
//    public void writeRecord(OWLOntologyChangesRecord record, final OutputStream outputStream) throws IOException {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        DataOutputStream recordOutputStream = new DataOutputStream(bos);
//        recordOutputStream.writeLong(record.getTimestamp());
//        recordOutputStream.writeUTF(record.getUserId().getUserName());
//        recordOutputStream.writeUTF(record.getMessage());
//        List<OWLOntologyChange> changeList = record.getChangeList();
//        recordOutputStream.writeInt(changeList.size());
//
//        LookupTable lookupTable = new LookupTable();
//
//        for(OWLOntologyChange chg : changeList) {
//            if(chg.isAxiomChange()) {
//                OWLOntologyID id = chg.getOntology().getOntologyID();
//                OWLOntologyIDSerializer serializer = new OWLOntologyIDSerializer();
//                serializer.write(id, lookupTable, recordOutputStream);
//                OWLAxiom ax = chg.getAxiom();
//                OWLObjectBinaryType type = OWLObjectBinaryType.getType(ax);
//                recordOutputStream.writeByte(type.getMarker());
//                type.getSerializer().write(ax, lookupTable, recordOutputStream);
//            }
//        }
//        recordOutputStream.flush();
//
//        DataOutputStream dos = new DataOutputStream(outputStream);
//        byte [] recordbytes = bos.toByteArray();
//
//        dos.writeInt(recordbytes.length);
//        dos.writeByte(CHANGE_RECORD_MARKER);
//        dos.write(recordbytes);
//        dos.flush();
//    }
//
//    public OWLOntologyChangesRecord readRecord(InputStream inputStream, OWLDataFactory df) throws IOException {
//
//        DataInputStream dis = new DataInputStream(inputStream);
//        int recordLength = dis.readInt();
//        dis.skipBytes(1);
//
//        long timestamp = dis.readLong();
//        String userName = dis.readUTF();
//        String message = dis.readUTF();
//        int changeListSize = dis.readInt();
//        LookupTable lookupTable = new LookupTable();
//        List<OWLOntologyChange> changeList = new ArrayList<OWLOntologyChange>(changeListSize + 2);
//        for(int i = 0; i < changeListSize; i++) {
//            OWLOntologyID id = new OWLOntologyIDSerializer().read(lookupTable, dis, df);
//            OWLOntology ontology = project.getRootOntology();
//            int typeMarker = dis.readByte();
//            OWLObjectBinaryType type = OWLObjectBinaryType.getType(typeMarker);
//            OWLAxiom ax = (OWLAxiom) type.getSerializer().read(lookupTable, dis, df);
//            changeList.add(new AddAxiom(ontology, ax));
//        }
//
//        return new OWLOntologyChangesRecord(new UserId(userName), message, timestamp, changeList);
//    }
//
//
//    public void readSummary(InputStream inputStream) throws IOException {
//        DataInputStream dis = new DataInputStream(inputStream);
//        List<Long> timeStamps = new ArrayList<Long>();
//        try {
//            int recordCount = 0;
//            while(true) {
//                int recordSize = dis.readInt();
//                dis.skipBytes(1);
//                long timestamp = dis.readLong();
//                timeStamps.add(timestamp);
//                dis.skipBytes(recordSize - 8);
//                recordCount++;
//            }
//        }
//        catch (EOFException e) {
//            System.err.println("END OF FILE");
//        }
//
//
//        for(long ts : timeStamps) {
//            System.out.println("Changes made at " + new Date(ts));
//        }
//    }
//
//
//    public List<OWLOntologyChangesRecord> readAllRecords(InputStream inputStream, OWLDataFactory df) throws IOException {
//        DataInputStream dis = new DataInputStream(inputStream);
//        List<OWLOntologyChangesRecord> records = new ArrayList<OWLOntologyChangesRecord>();
//        try {
//            while(true) {
//                OWLOntologyChangesRecord record = readRecord(inputStream, df);
//                records.add(record);
//            }
//        }
//        catch (EOFException e) {
//            System.err.println("END OF FILE");
//        }
//        return records;
//    }
}
