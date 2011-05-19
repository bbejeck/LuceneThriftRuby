package bbejeck.thrift;

import bbejeck.thrift.gen.LuceneSearch;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;

public class LuceneThriftServer {
    private static final int PORT = 9090;
    private static int numberThreads = 5;

    public static void main(String[] args) throws Exception {
        TServerSocket serverSocket = new TServerSocket(PORT, 100000);
        LuceneSearch.Processor searchProcessor = new LuceneSearch.Processor(new SearchHandler(args[0]));
        if (args.length > 1) {
            numberThreads = Integer.parseInt(args[1]);
        }
        TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(serverSocket);
        serverArgs.maxWorkerThreads(numberThreads);
        TServer thriftServer = new TThreadPoolServer(serverArgs.processor(searchProcessor).protocolFactory(new TBinaryProtocol.Factory()));
        thriftServer.serve();
    }
}
