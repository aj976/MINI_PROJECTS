import { Sequelize } from "sequelize";
import config from "./config.json" assert { type: "json" };

const sequelize = new Sequelize(
  config.database,
  config.username,
  config.password,
  {
    host: config.host,
    dialect: config.dialect,
    logging: false
  }
);

// Optional but IMPORTANT: verify connection
try {
  await sequelize.authenticate();
  console.log("Database connected successfully");
} catch (error) {
  console.error("Unable to connect to DB:", error);
}

export default sequelize;