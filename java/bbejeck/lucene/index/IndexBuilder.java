package bbejeck.lucene.index;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class IndexBuilder {

    public static void main(String[] args) throws Exception {
        String namesFile = "names.csv";
        Document doc = new Document();
        Field[] fields = new Field[]{new Field("firstName", "", Field.Store.YES, Field.Index.ANALYZED_NO_NORMS),
                new Field("lastName", "", Field.Store.YES, Field.Index.ANALYZED_NO_NORMS),
                new Field("address", "", Field.Store.YES, Field.Index.ANALYZED_NO_NORMS),
                new Field("email", "", Field.Store.YES, Field.Index.ANALYZED_NO_NORMS)};
        addFieldsToDocument(doc, fields);

        BufferedReader reader = new BufferedReader(new FileReader(namesFile));

        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(new File("blog-index")),new IndexWriterConfig(Version.LUCENE_31, new StandardAnalyzer(Version.LUCENE_31)));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] personData = getPersonData(line);
            setFieldData(personData, fields);
            indexWriter.addDocument(doc);
        }
        indexWriter.optimize();
        indexWriter.close();
    }

    private static String[] getPersonData(String line) {
        return line.split(",");
    }

    private static void setFieldData(String[] data, Field[] fields) {
        int index = 0;
        for (Field field : fields) {
            field.setValue(data[index++]);
        }
    }

    private static void addFieldsToDocument(Document doc, Field[] fields) {
        for (Field field : fields) {
            doc.add(field);
        }
    }

}
