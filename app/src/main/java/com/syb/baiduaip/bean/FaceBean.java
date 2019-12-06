package com.syb.baiduaip.bean;

import java.util.List;

public class FaceBean {

    private int face_num;
    private List<Face> face_list;

    public int getFace_num() {
        return face_num;
    }

    public List<Face> getFace_list() {
        return face_list;
    }

    public static class Face {
        private String face_token;
        private Location location;
        private List<Landmark> landmark;
        private List<Landmark> landmark72;
        private Landmark150 landmark150;

        public String getFace_token() {
            return face_token;
        }

        public Location getLocation() {
            return location;
        }

        public List<Landmark> getLandmark() {
            return landmark;
        }

        public List<Landmark> getLandmark72() {
            return landmark72;
        }

        public List<Landmark> getLandmark150() {
            return landmark150.getLandmark150List();
        }

        public static class Location {
            private double left;
            private double top;
            private int width;
            private int height;
            private int rotation;

            @Override
            public String toString() {
                return "Location{" +
                        "left=" + left +
                        ", top=" + top +
                        ", width=" + width +
                        ", height=" + height +
                        ", rotation=" + rotation +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Face{" +
                    "face_token='" + face_token + '\'' +
                    ", location=" + location +
                    ", landmark=" + landmark +
                    ", landmark72=" + landmark72 +
                    ", landmark150=" + landmark150 +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FaceBean{" +
                "face_num=" + face_num +
                ", face_list=" + face_list +
                '}';
    }
}
