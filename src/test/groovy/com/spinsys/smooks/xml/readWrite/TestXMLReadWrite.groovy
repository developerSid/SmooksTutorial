package com.spinsys.smooks.xml.readWrite

import org.milyn.javabean.binding.xml.XMLBinding
import org.milyn.payload.StringSource

import spock.lang.Specification

class TestXMLReadWrite extends Specification
{
   private XMLBinding xmlBinding;
   
   def setup()
   {
      def stream=this.class.classLoader.getResourceAsStream("com/spinsys/smooks/xml/readWrite/smooks-config.xml")
      
      try
      {
         xmlBinding=new XMLBinding().add(stream).intiailize();
      }
      finally
      {
         stream.close()
      }
   }
   def "Test Order processing" ()
   {
      def order=xmlBinding.fromXML(new StringSource(
      '''<ord:order xmlns:ord="http://acme.com/order">
             <ord:header>
                 <ord:date>Wed Nov 15 13:45:28 EST 2006</ord:date>
                 <ord:customer number="123123">Joe</ord:customer>
             </ord:header>
             <ord:order-items>
                 <ord:order-item>
                     <ord:product>111</ord:product>
                     <ord:quantity>2</ord:quantity>
                     <ord:price>8.90</ord:price>
                 </ord:order-item>
                 <ord:order-item>
                     <ord:product>222</ord:product>
                     <ord:quantity>7</ord:quantity>
                     <ord:price>5.20</ord:price>
                 </ord:order-item>
             </ord:order-items>
         </ord:order>'''), Order)
      
      expect:
         Date.parseToStringDate("Wed Nov 15 13:45:28 EST 2006") == order.header.date
         order.header.customerNumber == 123123
         
         order.orderItems != null
         order.orderItems.size() == 2
         order.orderItems[0].productId == 111
         order.orderItems[0].quantity == 2
         order.orderItems[0].price == 8.90
         order.orderItems[1].productId == 222
         order.orderItems[1].quantity == 7
         order.orderItems[1].price == 5.20
   }
}
