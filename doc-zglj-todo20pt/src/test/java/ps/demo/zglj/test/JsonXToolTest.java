package ps.demo.zglj.test;


import ps.demo.zglj.common.JsonXTool;

public class JsonXToolTest {

    public static void main(String [] args) {

        String jsonStr = """
                {
                "name": "xiao",
                "age": 11,
                "addresses": [
                    {
                    "line1": "l11",
                    
                    },
                     {
                    "line1": "l12",
                    
                    }
                ],
                }
                """;


        String name = JsonXTool.getFieldByXpath(jsonStr, "name");
        System.out.println("name" + name);
        String line1 = JsonXTool.getFieldByXpath(jsonStr, "$.addresses[1].line1");
        System.out.println("line1" + line1);

    }

}
