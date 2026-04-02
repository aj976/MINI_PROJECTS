import express from "express";
import cors from "cors";
import helmet from 'helmet'
import bodyParser from 'body-parser'

const app = express();


app.use(bodyParser.json());
app.use(helmet()); // Use helmet to security headers

//cors configurations(basic)
app.use(cors({
  origin: process.env.CORS_ORIGIN?.split(",") || "http://localhost:5173",
  credentials: true,
  methods: ["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"],
  allowedHeaders:["Authorization", "Content-Type"],
// incase we have comma seperated stuff it'll split 
}))

// Example of custom middleware to handle server error
const errorHandler = (err, req, res, next) => {
  // print error stack
 console.error(err.stack);
// send 500 status code with message Internal Server error in response res.status(500).send('Internal Server Error.');
};

// Use the custom middleware
app.use(errorHandler);

export default app;