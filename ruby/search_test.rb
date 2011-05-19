$:.unshift File.dirname(__FILE__)
require 'test/unit'
require 'thrift_connection'
require 'gen-rb/lucene_search_types'

class SearchTest < Test::Unit::TestCase

  def setup
    @lucene_client = ThriftConnection::LuceneClient.new
  end


  def teardown
    @lucene_client.close
  end

  def test_search_client_first_name
    persons = @lucene_client.search("firstName:Tia")
    assert_equal(5, persons.length)

    persons.each do |person|
      assert_equal("Tia", person.firstName)
    end
  end

  def test_search_person_class_first_name
    persons = Person.find_by_first_name("Tia")
    assert_equal(5, persons.length)

    persons.each do |person|
      assert_equal("Tia", person.firstName)
    end
  end

  def test_search_client_first_name_email_domain
    persons = @lucene_client.search("+firstName:Elizabeth +email:*pookmail.com")
    assert_equal(59, persons.length)
  end

  def test_search_person_class_first_name_email_domain
    persons = Person.find_by_first_name_and_email("elizabeth", "*pookmail.com")
    assert_equal(59, persons.length)
  end

  def test_search_client_first_name_and_last_name
    persons = @lucene_client.search("+firstName:Elizabeth +lastName:Krause")
    assert_equal(1, persons.length)
    person = persons[0]

    assert_equal("Elizabeth", person.firstName)
    assert_equal("Krause", person.lastName)
  end

  def test_search_person_class_first_name_and_last_name
    persons = Person.find_by_first_name_and_last_name("elizabeth", "krause")
    assert_equal(1, persons.length)
    person = persons[0]

    assert_equal("Elizabeth", person.firstName)
    assert_equal("Krause", person.lastName)
  end

  def test_search_person_class_first_name_or_last_name
    persons = Person.find_by_first_name_or_last_name("elizabeth", "krause")
    assert_equal(289, persons.length)
  end

  def test_invalid_search
    assert_raises ArgumentError do
      Person.find_person_by_name("tia")
    end
  end

end
