/*
 * Copyright (C) 2017-2019 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dremio.exec.physical.impl;

import com.dremio.sabot.BaseTestFunction;
import org.junit.Test;

import java.math.BigDecimal;

public class TestStringFunctions extends BaseTestFunction {
  /**
   * Returns the string 's' repeated 'n' times
   */
  private String repeat(String s, int n) {
    return new String(new char[n]).replace("\0", s);
  }

  @Test
  public void charLength(){
    testFunctions(new Object[][]{
      {"char_length('aababcdf')", 8},
      {"char_length('')", 0},
      {"char_length(c0)", "abc", 3},
      {"character_length('aababcdf')", 8},
      {"character_length('')", 0},
      {"character_length(c0)", "abc", 3},
      {"length('aababcdf')", 8},
      {"length('')", 0},
      {"length(c0)", "abc", 3}
    });
  }

  @Test
  public void md5HashTest(){
    testFunctionsInterpretedOnly(new Object[][]{
      {"to_hex(binary_string(md5(c0)))", "ði ıntəˈnæʃənəl fəˈnɛtık əsoʊsiˈeıʃnY [ˈʏpsilɔn], Yen [jɛn], Yoga [ˈjoːgɑ]", "a633460644425b44e0e023d6980849cc".toUpperCase()},
      {"to_hex(binary_string(hashMD5(c0)))", "ði ıntəˈnæʃənəl fəˈnɛtık əsoʊsiˈeıʃnY [ˈʏpsilɔn], Yen [jɛn], Yoga [ˈjoːgɑ]".getBytes(), "a633460644425b44e0e023d6980849cc".toUpperCase()},
      {"to_hex(binary_string(md5(c0)))", Long.MAX_VALUE, "BD5F6598B2D2CD7F130BA3E152116FF7"},
      {"to_hex(binary_string(md5(c0)))", Integer.MAX_VALUE, "37497AD6A0C4F123CD1A39278AFC6869"},
      {"to_hex(binary_string(hashMD5(c0)))", Float.MAX_VALUE, "8A18AEED5CAFF58AA370B52E36DCBC3F"},
      {"to_hex(binary_string(md5(c0)))", Double.MAX_VALUE, "5801BDC2D76B6EA9B7091A5026CAF731"},
      {"to_hex(binary_string(md5(c0)))", BigDecimal.valueOf(Long.MAX_VALUE), "316E8EB7B578786FE5036D150D71E44A"},
    });
  }

  @Test
  public void sha1HashTest(){
    testFunctionsInterpretedOnly(new Object[][]{
      {"to_hex(binary_string(sha1(c0)))", "ði ıntəˈnæʃənəl fəˈnɛtık əsoʊsiˈeıʃnY [ˈʏpsilɔn], Yen [jɛn], Yoga [ˈjoːgɑ]", "8C6A880CE350769627CAA70F3526AEEDCC4C959D"},
      {"to_hex(binary_string(hashSHA1(c0)))", "ði ıntəˈnæʃənəl fəˈnɛtık əsoʊsiˈeıʃnY [ˈʏpsilɔn], Yen [jɛn], Yoga [ˈjoːgɑ]".getBytes(), "8C6A880CE350769627CAA70F3526AEEDCC4C959D"},
      {"to_hex(binary_string(sha1(c0)))", Long.MAX_VALUE, "BF7F9F8D6E0A3426AEF3F0CE773E69E85821EFC7"},
      {"to_hex(binary_string(hashSHA1(c0)))", Integer.MAX_VALUE, "6C28217EECDB75AC5378D20243029696F5E633A1"},
      {"to_hex(binary_string(sha1(c0)))", Float.MAX_VALUE, "26AE45CABD7D1BD5EA3D6511D0FDBF5F196F8AD5"},
      {"to_hex(binary_string(hashSHA1(c0)))", Double.MAX_VALUE, "DE1E67A299928343DD050A52FDE96FEB3F1F3028"},
      {"to_hex(binary_string(sha1(c0)))", BigDecimal.valueOf(Long.MAX_VALUE), "9CCEC2A0EAA5E598885108FE112146A272BBFB50"},
    });
  }
  @Test
  public void sha256HashTest(){
    testFunctionsInterpretedOnly(new Object[][]{
      {"to_hex(binary_string(sha256(c0)))", "ði ıntəˈnæʃənəl fəˈnɛtık əsoʊsiˈeıʃnY [ˈʏpsilɔn], Yen [jɛn], Yoga [ˈjoːgɑ]", "68E676F563660BBEBF718D3E062E1A56339B7BA61E48A116C9844298B1D41641"},
      {"to_hex(binary_string(hashSHA256(c0)))", "ði ıntəˈnæʃənəl fəˈnɛtık əsoʊsiˈeıʃnY [ˈʏpsilɔn], Yen [jɛn], Yoga [ˈjoːgɑ]".getBytes(), "68E676F563660BBEBF718D3E062E1A56339B7BA61E48A116C9844298B1D41641"},
      {"to_hex(binary_string(sha256(c0)))", Long.MAX_VALUE, "C624EEBF5B9282431FC4E19C3C707A012275E198D3A077DCD36A7B74E4A804AD"},
      {"to_hex(binary_string(hashSHA256(c0)))", Integer.MAX_VALUE, "24AE0D93F1AF72ADDC019182FAE1AB44547A1E84758785745F4358373EAB1960"},
      {"to_hex(binary_string(sha256(c0)))", Float.MAX_VALUE, "DDE5BCFF31E3B2E6085239AF1DA9827447D0705E8500092AB6BDF699D819C231"},
      {"to_hex(binary_string(hashSHA256(c0)))", Double.MAX_VALUE, "88DC7B5696598BB5BB3B0A3100300DB6FA58D87CAC4CEE380462588CF04B9015"},
      {"to_hex(binary_string(sha256(c0)))", BigDecimal.valueOf(Long.MAX_VALUE+Integer.MAX_VALUE), "4BEF15C9E93BF845C1EBF6EC9DA538FACF86154682CB23DA4A8516EC3A1B3C09"},
    });
  }

  @Test
  public void hexConversion(){
    testFunctions(new Object[][]{
      {"to_hex(binary_string('\\\\x11\\\\x22'))", "1122"},
      {"string_binary(from_hex('1112'))", "\\x11\\x12"},
      {"to_hex(repeatstr(binary_string('\\\\x11\\\\x22'),256))", repeat("1122", 256)},
      {"to_hex(c0)", 6713199L, "666F6F"},
    });
  }

  @Test
  public void testAscii(){
    // ASCII Hive function which returns the decimal code of the first character of input string
    testFunctions(new Object[][]{
      {"ascii(c0)", "hello", 104},
      {"ascii(c0)", "12345", 49},
      {"ascii(c0)", "ABC", 65},
    });
  }

  @Test
  public void testSpace(){
    // Space Hive function - returns a string with a specified number of spaces
    testFunctions(new Object[][]{
      {"space(c0)", 1, " "},
      {"space(c0)", 2, "  "},
      {"space(c0)", 3, "   "},
    });
  }

  @Test
  public void testBinaryRepresentation(){
    // Bin Hive function - returns the binary representation of a specified integer or long
    testFunctions(new Object[][]{
      {"bin(c0)", 0, "0"},
      {"bin(c0)", 7, "111"},
      {"bin(c0)", 28550, "110111110000110"},
      {"bin(c0)", -28550, "1111111111111111111111111111111111111111111111111001000001111010"},
      {"bin(c0)", Long.MAX_VALUE, "111111111111111111111111111111111111111111111111111111111111111"},
      {"bin(c0)", Long.MIN_VALUE, "1000000000000000000000000000000000000000000000000000000000000000"},
    });
  }

  @Test
  public void testBase64Unbase64(){
    // Base64 and Unbase64 Hive functions - returns the respective encoded and decoded base64 values
    testFunctionsInterpretedOnly(new Object[][]{
      {"base64(c0)", "hello".getBytes(), "aGVsbG8="},
      {"base64(c0)", "test".getBytes(), "dGVzdA=="},
      {"base64(c0)", "hive".getBytes(), "aGl2ZQ=="},
      {"unbase64(c0)", "aGVsbG8=", "hello".getBytes()},
      {"unbase64(c0)", "dGVzdA==", "test".getBytes()},
      {"unbase64(c0)", "aGl2ZQ==", "hive".getBytes()},
    });
  }


  @Test
  public void like(){
    testFunctions(new Object[][]{
      {"like('abc', 'abc')", true},
      {"like('abc', 'a%')", true},
      {"like('abc', '_b_')", true},
      {"like('abc', 'c')", false},

      //See issue DX-12628 (dot must be treated as a literal in LIKE)
      {"like('abcde', 'abc.')", false},
      {"like('abc.e', 'abc.')", false},
      {"like('abcd', 'abc.')", false},
      {"like('abc.', 'abc.')", true},
      {"like('abc', 'abc.')", false},

      {"like('abcde', 'abc.%')", false},
      {"like('abc.e', 'abc.%')", true},
      {"like('abcd', 'abc.%')", false},
      {"like('abc.', 'abc.%')", true},
      {"like('abc', 'abc.%')", false}
    });
  }


  @Test
  public void similar(){
    testFunctions(new Object[][]{
    { "similar('abc', 'abc')", true },
    { "similar('abc', 'a')", false },
    { "similar('abc', '%(b|d)%')", true },
    { "similar('abc', '(b|c)%')", false }
    });
  }

  @Test
  public void ltrim(){
    testFunctions(new Object[][]{
      { "ltrim('   abcdef  ')", "abcdef  "},
      { "ltrim('abcdef ')", "abcdef "},
      { "ltrim('    ')", ""},
      { "ltrim('abcd')", "abcd"},
      { "ltrim('  çåå†b')", "çåå†b"},
      { "ltrim('')", ""},
      { "ltrim('abcdef', 'abc')", "def"},
      { "ltrim('abcdef', '')", "abcdef"},
      { "ltrim('abcdabc', 'abc')", "dabc"},
      { "ltrim('abc', 'abc')", ""},
      { "ltrim('abcd', 'efg')", "abcd"},
      { "ltrim('ååçåå†eç†Dd', 'çåå†')", "eç†Dd"},
      { "ltrim('ç†ååçåå†', 'çå†')", ""},
      { "ltrim('åéçå', 'åé')", "çå"},
      { "ltrim('', 'abc')", ""},
      { "ltrim('', '')", ""}
    });
  }

  @Test
  public void trim(){
    testFunctions(new Object[][]{
      { "btrim('   abcdef  ')", "abcdef"},
      { "btrim('abcdef ')", "abcdef"},
      { "btrim('  abcdef ')", "abcdef"},
      { "btrim('    ')", ""},
      { "btrim('abcd')", "abcd"},
      { "btrim('  çåå†b  ')", "çåå†b"},
      { "btrim('')", ""},
      { "btrim('     efghI e', 'e ')", "fghI"},
      { "btrim('a', 'a')", ""},
      { "btrim('', '')", ""},
      { "btrim('abcd', 'efg')", "abcd"},
      { "btrim('ååçåå†Ddeç†', 'çåå†')", "Dde"},
      { "btrim('ç†ååçåå†', 'çå†')", ""},
      { "btrim('åe†çå', 'çå')", "e†"},
      { "btrim('aAa!aAa', 'aA')", "!"},
      { "btrim(' aaa ', '')", " aaa "}
    });
  }


  @Test
  public void replace(){
    testFunctions(new Object[][]{
      {"replace('aababcdf', 'ab', 'AB')", "aABABcdf"},
      {"replace('aababcdf', 'a', 'AB')", "ABABbABbcdf"},
      {"replace('aababcdf', '', 'AB')", "aababcdf"},
      {"replace('aababcdf', 'ab', '')", "acdf"},
      {"replace('abc', 'abc', 'ABCD')", "ABCD"},
      {"replace('abc', 'abcdefg', 'ABCD')", "abc"}
    });
  }


  @Test
  public void rtrim(){
    testFunctions(new Object[][]{
      {"rtrim('   abcdef  ')", "   abcdef"},
      {"rtrim('  abcdef')", "  abcdef"},
      {"rtrim('    ')", ""},
      {"rtrim('abcd')", "abcd"},
      {"rtrim('  ')", ""},
      {"rtrim('çåå†b  ')", "çåå†b"},
      {"rtrim('abcdef', 'def')", "abc"},
      {"rtrim('abcdef', '')", "abcdef"},
      {"rtrim('ABdabc', 'abc')", "ABd"},
      {"rtrim('abc', 'abc')", ""},
      {"rtrim('abcd', 'efg')", "abcd"},
      {"rtrim('eDdç†ååçåå†', 'çåå†')", "eDd"},
      {"rtrim('ç†ååçåå†', 'çå†')", ""},
      {"rtrim('åéçå', 'çå')", "åé"},
      {"rtrim('', 'abc')", ""},
      {"rtrim('', '')", ""}
    });
  }

  @Test
  public void concat(){
    testFunctions(new Object[][]{
      { "concat('abc', 'ABC')", "abcABC"},
      { "concat('abc', '')", "abc"},
      { "concat('', 'ABC')", "ABC"},
      { "concat('', '')", ""}
    });
  }

  @Test
  public void lower(){
    testFunctions(new Object[][]{
      { "lower('ABcEFgh')", "abcefgh"},
      { "lower('aBc')", "abc"},
      { "lower('')", ""}
    });
  }

  @Test
  public void position() {
    testFunctions(new Object[][]{
      {"position('abc', 'AabcabcB')", 2},
      {"position('A', 'AabcabcB')", 1},
      {"position('', 'AabcabcB')", 0},
      {"position('abc', '')", 0},
      {"position('', '')", 0},
      {"position('abc', 'AabcabcB', 3)", 5},
      {"position('A', 'AabcabcB', 1)", 1},
      {"position('', 'AabcabcB', 1)", 0},
      {"position('abc', '', 1)", 0},
      {"position('', '', 5)", 0},
      {"position('foo', 'foofoo', 1)", 1},
      {"position('foo', 'foofoo', 2)", 4},
      {"position('foo', 'foofoo', 3)", 4},
      {"position('foo', 'foofoo', 4)", 4},
      {"position('foo', 'foofoo', 5)", 0},
      {"position('abc', '', 1)", 0},
      {"position('', '', 5)", 0},
      {"locate('abc', 'AabcabcB')", 2},
      {"locate('A', 'AabcabcB')", 1},
      {"locate('', 'AabcabcB')", 0},
      {"locate('abc', '')", 0},
      {"locate('', '')", 0},
      {"locate('abc', 'AabcabcB', 3)", 5},
      {"position('A', 'AabcabcB', 1)", 1},
      {"locate('', 'AabcabcB', 1)", 0},
      {"locate('abc', '', 1)", 0},
      {"locate('', '', 5)", 0},
      {"strpos('AabcabcB', 'abc')", 2},
      {"strpos('', 'AabcabcB')", 0},
      {"strpos('', 'abc')", 0},
      {"strpos('', '')", 0}
    });
  }

  @Test
  public void right(){
    testFunctions(new Object[][]{
      {"right('abcdef', 2)", "ef"},
      {"right('abcdef', 6)", "abcdef"},
      {"right('abcdef', 7)", "abcdef"},
      {"right('abcdef', -2)", "cdef"},
      {"right('abcdef', -5)", "f"},
      {"right('abcdef', -6)", ""},
      {"right('abcdef', -7)", ""}
    });
  }

  @Test
  public void substr(){
    testFunctions(new Object[][]{
      { "substring('abcdef', 1, 3)", "abc"},
      { "substring('abcdef', 2, 3)", "bcd"},
      { "substring('abcdef', 2, 5)", "bcdef"},
      { "substring('abcdef', 2, 10)", "bcdef"},
      { "substring('abcdef', 0, 3)", "abc"},
      { "substring('abcdef', -1, 3)", "f"},
      { "substring('', 1, 2)", ""},
      { "substring('abcdef', 10, 2)", ""},
      { "substring('भारतवर्ष', 1, 4)", "भारत"},
      { "substring('भारतवर्ष', 5, 4)", "वर्ष"},
      { "substring('भारतवर्ष', 5, 5)", "वर्ष"},
      { "substring('abcdef', 3)", "cdef"},
      { "substring('abcdef', -2)", "ef"},
      { "substring('abcdef', 0)", "abcdef"},
      { "substring('abcdef', 10)", ""},
      { "substring('अपाचे ड्रिल', 7)", "ड्रिल"}
    });
  }


  @Test
  public void left(){
    testFunctions(new Object[][]{
      { "left('abcdef', 2)", "ab"},
      { "left('abcdef', 6)", "abcdef"},
      { "left('abcdef', 7)", "abcdef"},
      { "left('abcdef', -2)", "abcd"},
      { "left('abcdef', -5)", "a"},
      { "left('abcdef', -6)", ""},
      { "left('abcdef', -7)", ""}
    });
  }


  @Test
  public void lpad(){
    testFunctions(new Object[][]{
      { "lpad('abcdef', 0, 'abc')", ""},
      { "lpad('abcdef', -3, 'abc')", ""},
      { "lpad('abcdef', 6, 'abc')", "abcdef"},
      { "lpad('abcdef', 2, 'abc')", "ab"},
      { "lpad('abcdef', 2, '')", "ab"},
      { "lpad('abcdef', 10, '')", "abcdef"},
      { "lpad('abcdef', 10, 'A')", "AAAAabcdef"},
      { "lpad('abcdef', 10, 'AB')", "ABABabcdef"},
      { "lpad('abcdef', 10, 'ABC')", "ABCAabcdef"},
      { "lpad('abcdef', 10, 'ABCDEFGHIJKLMN')", "ABCDabcdef"}

    });
  }

  @Test
  public void regexreplace(){
    testFunctions(new Object[][]{
      {"regexp_replace('Thomas', '.[mN]a.', 'M')", "ThM" },
      {"regexp_replace('Thomas', '.[mN]a.', '')", "Th"},
      {"regexp_replace('Thomas', 'ef', 'AB')", "Thomas" }
    });
  }

  @Test
  public void testRegexpExtract() {
    testFunctions(new Object[][]{
      {"regexp_extract(c0, 'foo(.*?)(bar)', 2)", "foothebar", "bar"},
      {"regexp_extract(c0, '@(.*)', 0)", "john@test.com", "@test.com"},
      {"regexp_extract(c0, '(.*) (D.*)', 2)", "John Doe", "Doe"},
    });
  }

  @Test
  public void rpad(){
    testFunctions(new Object[][]{
      { "rpad('abcdef', 0, 'abc')", ""},
      { "rpad('abcdef', -3, 'abc')", ""},
      { "rpad('abcdef', 6, 'abc')", "abcdef"},
      { "rpad('abcdef', 2, 'abc')", "ab"},
      { "rpad('abcdef', 2, '')", "ab"},
      { "rpad('abcdef', 10, '')", "abcdef"},
      { "rpad('abcdef', 10, 'A')", "abcdefAAAA"},
      { "rpad('abcdef', 10, 'AB')", "abcdefABAB"},
      { "rpad('abcdef', 10, 'ABC')", "abcdefABCA"},
      { "rpad('abcdef', 10, 'ABCDEFGHIJKLMN')", "abcdefABCD"}

    });
  }

  @Test
  public void upper(){
    testFunctions(new Object[][]{
      { "upper('ABcEFgh')", "ABCEFGH"},
      { "upper('aBc')", "ABC"},
      { "upper('')", ""}
    });
  }

  @Test
  public void stringfuncs(){
    testFunctions(new Object[][]{
      {" ascii('apache') ", 97},
      {" ascii('Apache') ", 65},
      {" ascii('अपाचे') ", -32},
      {" chr(65) ", "A"},
      {" btrim('xyxbtrimyyx', 'xy') ", "btrim"},
      {" repeatstr('Peace ', 3) ", "Peace Peace Peace "},
      {" repeatstr('हकुना मताता ', 2) ", "हकुना मताता हकुना मताता "},
      {" reverse('tictak') ", "katcit"},
      {" toascii('âpple','ISO-8859-1') ", "\u00C3\u00A2pple"},
      {" reverse('मदन') ", "नदम"},
      {"substring(c0, 1, 4)", "alpha", "alph"},
      {"byte_substr(c0, -3, 2)", "alpha".getBytes(), "ph".getBytes()}
      // {"substring(c0, -3, 2)", "alphabeta", "ph"} (Invalid since we follow Postgres)

    });
  }

}
