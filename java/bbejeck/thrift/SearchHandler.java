package bbejeck.thrift;

import bbejeck.thrift.gen.LuceneSearch;
import bbejeck.thrift.gen.LuceneSearchException;
import bbejeck.thrift.gen.Person;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchHandler implements LuceneSearch.Iface {
    private IndexSearcher searcher;
    private QueryParser queryParser;
    private static final int MAX_RESULTS = 1000;

    public SearchHandler(String indexPath) {
        try {
            searcher = new IndexSearcher(FSDirectory.open(new File(indexPath)), true);
            queryParser = new QueryParser(Version.LUCENE_31, null, new StandardAnalyzer(Version.LUCENE_31));
            queryParser.setAllowLeadingWildcard(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> search(String query) throws LuceneSearchException {
        List<Person> results = new ArrayList<Person>();
        try {
            Query q = queryParser.parse(query);
            TopDocs topDocs = searcher.search(q, MAX_RESULTS);
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document document = searcher.doc(sd.doc);
                results.add(getPersonFromDocument(document));
            }
        } catch (Exception e) {
            throw new LuceneSearchException(e.getMessage());
        }
        return results;
    }

    private Person getPersonFromDocument(Document document) {
        Person p = new Person();
        p.firstName = document.get("firstName");
        p.lastName = document.get("lastName");
        p.address = document.get("address");
        p.email = document.get("email");

        return p;
    }
}
