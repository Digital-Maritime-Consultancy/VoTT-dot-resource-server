package org.dmc.vottdotserver.models.domain;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.json.JSONObject;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.HashMap;
import java.util.Map;

@Entity
@Data
public class Task extends Resource{
    private String id;

    private String stella_url;
    private String description;
    //private Map<String, String> class_list;
    private String type;
    private String status;

    public Task() {

    }
    //private Map<String, String> image_list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Task(JSONObject json) {
        this.id = (String) json.get("id");
        //this.class_list = new HashMap<String, String>();
        //this.image_list = new HashMap<String, String>();
        this.data = json.toString();
    }

    public String getData() {
        return data;
    }

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private String data;
}
