// TODO: Include your class to test here.
#define BOOST_TEST_MODULE MyTest
#include <boost/test/unit_test.hpp>

BOOST_AUTO_TEST_CASE(MyTestCasePass)
{
	BOOST_CHECK_EQUAL(2,2);
}

BOOST_AUTO_TEST_CASE(MyTestCaseFail)
{
	BOOST_CHECK_EQUAL(1,2);
}

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
