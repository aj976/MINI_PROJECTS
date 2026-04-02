import dotenv from "dotenv";
import express from "express";
import app from "./app.js";
import sequelize from "./config/connection.js";

await sequelize.sync();

dotenv.config({
    path: "./.env",
});

const port = process.env.PORT || 3000

app.listen(port, () => {
      console.log(`Server running at http://localhost:${port}`);
    });