package com.spinsys.smooks.xml.readWrite.transform

import javax.xml.transform.stream.StreamResult

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import org.milyn.Smooks
import org.milyn.payload.StringSource

import spock.lang.Specification


class TestXML2XML extends Specification
{
   Smooks smooks
   
   def setup()
   {
      def stream=this.class.classLoader.getResourceAsStream("com/spinsys/smooks/xml/readWrite/transform/xml/readWrite/xml2xml/smooks-config.xml")
      
      try
      {
         this.smooks=new Smooks(stream);
      }
      finally
      {
         stream.close()
      }
   }
   def "test xml to xml" ()
   {
      given:
         def xml='''<order id='332'>
                        <header>
                            <customer number="123">Joe</customer>
                        </header>
                        <order-items>
                            <order-item id='1'>
                                <product>1</product>
                                <quantity>2</quantity>
                                <price>8.80</price>
                            </order-item>
                        </order-items>
                    </order>'''
      when:
         def result=new StringWriter()
         smooks.filterSource(new StringSource(xml), new StreamResult(result))
      then:
         areIdentical(result, '''<salesorder>
                                     <details>
                                         <orderid>332</orderid>
                                         <customer>
                                             <id>123</id>
                                             <name>Joe</name>
                                         </customer>
                                     <details>
                                     <itemList>
                                         <item>
                                             <id>1</id>
                                             <productId>1</productId>
                                             <quantity>2</quantity>
                                             <price>8.80</price>
                                         <item>        
                                     </itemList>
                                 </salesorder>''')
   }
   def areIdentical(expected, actual)
   {
      XMLUnit.setIgnoreWhitespace true
      new Diff(expected.toString(), actual.toString()).identical()
   }
   def cleanup()
   {
      this.smooks.close();
   }
}
