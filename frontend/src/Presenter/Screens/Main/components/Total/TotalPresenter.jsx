import React from "react";
import ClothItem from "../ClothItem";

const TotalPresenter = ({ clothList }) => {
  return (
    <>
      {clothList.map((cloth, index) => (
        <ClothItem cloth={cloth} key={index} />
      ))}
    </>
  );
};

export default TotalPresenter;
