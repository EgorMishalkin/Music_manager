import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class XMLManager {
    public static void saveGroupsToXML(List<String> groups, File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.newDocument();
        Element root = doc.createElement("groups");
        doc.appendChild(root);

        for (String group : groups) {
            Element groupElement = doc.createElement("group");

            // Разделяем название и год, если есть
            String name = group;
            String year = "";
            if (group.contains("(") && group.endsWith(")")) {
                int idx = group.lastIndexOf("(");
                name = group.substring(0, idx).trim();
                year = group.substring(idx + 1, group.length() - 1);
            }

            Element nameElement = doc.createElement("name");
            nameElement.setTextContent(name);
            groupElement.appendChild(nameElement);

            Element yearElement = doc.createElement("year");
            yearElement.setTextContent(year);
            groupElement.appendChild(yearElement);

            root.appendChild(groupElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);

        transformer.transform(source, result);
    }

    // Загрузка списка групп из XML
    public static void loadGroupsFromXML(File file, List<String> groups) throws Exception {
        groups.clear();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        NodeList groupNodes = doc.getElementsByTagName("group");
        for (int i = 0; i < groupNodes.getLength(); i++) {
            Node node = groupNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                String year = element.getElementsByTagName("year").item(0).getTextContent();

                if (!year.isEmpty()) {
                    groups.add(name + " (" + year + ")");
                } else {
                    groups.add(name);
                }
            }
        }
    }
}
