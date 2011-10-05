// TODO: Include your class to test here.
#define BOOST_TEST_MODULE MyTest
#include <boost/test/unit_test.hpp>
#include <string>
using namespace std;

BOOST_AUTO_TEST_CASE(MyTestCasePass)
{
	BOOST_CHECK_EQUAL(2,2);
}

BOOST_AUTO_TEST_CASE(MyTestCaseFail)
{
	BOOST_CHECK_EQUAL(1,2);
	BOOST_CHECK_EQUAL(3,2);
}

BOOST_AUTO_TEST_CASE(MyTestCaseFailException)
{
	int i = 0;
	BOOST_CHECK_THROW(++i, std::bad_cast);
	BOOST_CHECK_EQUAL(3,2);
}

BOOST_AUTO_TEST_CASE(MyTestCaseThrowException)
{
	BOOST_CHECK_EQUAL(1,1);
	throw std::bad_cast();
}
/*
BOOST_AUTO_TEST_CASE(MyTestCaseSegfault)
{
	bad_cast* c;
	c->what();
}*/

BOOST_AUTO_TEST_SUITE(test1_suite)

BOOST_AUTO_TEST_CASE(Test1)
{
    BOOST_CHECK(2 < 1);
}

BOOST_AUTO_TEST_SUITE_END()


BOOST_AUTO_TEST_SUITE(test2_suite)

BOOST_AUTO_TEST_CASE(Test2)
{
    BOOST_CHECK(2 < 1);
}

BOOST_AUTO_TEST_SUITE_END()
