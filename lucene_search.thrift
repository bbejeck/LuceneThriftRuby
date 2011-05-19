namespace java bbejeck.thrift.gen

struct Person {
  1: string firstName,
  2: string lastName,
  3: string address,
  4: string email
}

exception LuceneSearchException {
  1: string message
}

service LuceneSearch { 
    list<Person> search(1: string query) throws (1:LuceneSearchException error) 
}
