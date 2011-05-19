$:.unshift File.dirname(__FILE__)
require 'thrift'
require 'gen-rb/lucene_search'

module ThriftConnection

  class LuceneClient

    def initialize(host='localhost', port=9090)
      socket = Thrift::Socket.new(host, port)
      @transport = Thrift::BufferedTransport.new(socket)
      protocol_factory = ::Thrift::BinaryProtocolFactory.new
      protocol = protocol_factory.get_protocol(@transport)
      @transport.open
      @lucene_client = LuceneSearch::Client.new(protocol)
    end

    def search(query)
      @lucene_client.search(query)
    end

    def close
      @transport.close
    end

  end

end