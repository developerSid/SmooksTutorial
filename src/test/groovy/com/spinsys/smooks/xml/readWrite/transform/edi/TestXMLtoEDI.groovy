package com.spinsys.smooks.xml.readWrite.transform.edi

import javax.xml.transform.stream.StreamResult

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import org.milyn.Smooks
import org.milyn.payload.StringSource

import spock.lang.Specification

class TestXMLtoEDI extends Specification
{
   private Smooks smooks;
   
   def setup()
   {
      def stream=this.class.classLoader.getResourceAsStream("smooks/config/edi/smooks-config.xml")
      
      try
      {
         smooks=new Smooks(stream);
      }
      finally
      {
         stream.close()
      }
   }
   def "test edi to xml transformation" ()
   {
      given:
         def edi='''HDR*1*0*59.97*64.92*4.95*Wed Nov 15 13:45:28 EST 2006
                    CUS*user1*Harry^Fletcher*SD
                    ORD*1*1*364*The 40-Year-Old Virgin*29.98
                    ORD*2*1*299*Pulp Fiction*29.99'''
      when:
         def result=new StringWriter()
         smooks.filterSource(new StringSource(edi), new StreamResult(result))
      then:
         areIdentical(result, '''<Order>
                                    <header>
                                        <order-id>1</order-id>
                                        <status-code>0</status-code>
                                        <net-amount>59.97</net-amount>
                                        <total-amount>64.92</total-amount>
                                        <tax>4.95</tax>
                                        <date>Wed Nov 15 13:45:28 EST 2006</date>
                                    </header>
                                    <customer-details>
                                       <username>user1</username>
                                       <name>
                                          <firstname>Harry</firstname>
                                          <lastname>Fletcher</lastname>
                                       </name>
                                       <state>SD</state>
                                    </customer-details>
                                    <order-item>
                                       <position>1</position>
                                       <quantity>1</quantity>
                                       <product-id>364</product-id>
                                       <title>The 40-Year-Old Virgin</title>
                                       <price>29.98</price>
                                    </order-item>
                                    <order-item>
                                       <position>2</position>
                                       <quantity>1</quantity>
                                       <product-id>299</product-id>
                                       <title>Pulp Fiction</title>
                                       <price>29.99</price>
                                    </order-item>
                                 </Order>
                              ''')
   }
   def areIdentical(expected, actual) 
   {
      XMLUnit.setIgnoreWhitespace true
      new Diff(expected.toString(), actual.toString()).identical()
   }
   def cleanup()
   {
      smooks.close();
   }
}
