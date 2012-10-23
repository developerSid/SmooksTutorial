package com.spinsys.smooks.xml.readWrite.transform

import java.text.SimpleDateFormat

import org.milyn.javabean.binding.xml.XMLBinding
import org.milyn.payload.StringSource

import spock.lang.Specification

import com.spinsys.smooks.xml.readWrite.Order

class TestXMLReadWriteTransform2 extends Specification
{
   private XMLBinding xmlBinding;
   
   def setup()
   {
      def stream=this.class.classLoader.getResourceAsStream("com/spinsys/smooks/xml/readWrite/transform/smooks-config-2.xml")
      
      try
      {
         xmlBinding=new XMLBinding().add(stream).intiailize();
      }
      finally
      {
         stream.close()
      }
   }
   def "test read write transform configuration 1" ()
   {
      def order=xmlBinding.fromXML(new StringSource(
         '''<ord:orderV2 xmlns:ord="http://acme.com/v2/order" date="15-11-2006">
               <ord:customer-details>
                   <ord:id>123123</ord:id>
                   <ord:name>Joe</ord:name>
               </ord:customer-details>
               <ord:items>
                   <ord:item>
                       <ord:prodcode>111</ord:prodcode>
                       <ord:quantity>2</ord:quantity>
                       <ord:price>8.9</ord:price>
                   </ord:item>
                   <ord:item>
                       <ord:prodcode>222</ord:prodcode>
                       <ord:quantity>7</ord:quantity>
                       <ord:price>5.2</ord:price>
                   </ord:item>
               </ord:items>
            </ord:orderV2>'''), Order)
         
         expect:
            order.header != null
            order.header.date == new SimpleDateFormat("dd-MM-yyyy").parse("15-11-2006")
            order.header.customerNumber == 123123
            
            order.orderItems.size() == 2
            order.orderItems[0].price == 8.90
            order.orderItems[0].quantity == 2
            order.orderItems[0].productId == 111
            
            order.orderItems[1].price == 5.20
            order.orderItems[1].quantity == 7
            order.orderItems[1].productId == 222
   }
}
